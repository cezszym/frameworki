package org.example.service;

import lombok.extern.java.Log;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.queryparser.flexible.standard.config.PointsConfig;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler;
import org.apache.lucene.search.*;
import org.example.entity.Flat;
import org.example.entity.Post;
import org.example.repository.FlatRepository;
import org.example.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

@Service
@Log
public class BrowserServiceImpl implements BrowserService {

    private final PostRepository postRepository;
    private final FlatRepository flatRepository;
    private final IndexWriter indexWriter;
    private final Analyzer analyzer;
    private final SearcherManager searcherManager;

    public BrowserServiceImpl(PostRepository postRepository, FlatRepository flatRepository, IndexWriter indexWriter, Analyzer analyzer, SearcherManager searcherManager) {
        this.postRepository = postRepository;
        this.flatRepository = flatRepository;
        this.indexWriter = indexWriter;
        this.analyzer = analyzer;
        this.searcherManager = searcherManager;
    }

    @Override
    public void createIndex() {
        List<Post> posts = postRepository.findAll();
        try {
            createPostIndex(posts);
        } catch (IOException e) {
            log.log(Level.SEVERE, "Error while creating post index", e);
        }
        List<Flat> flats = flatRepository.findAll();
        try {
            createFlatIndex(flats);
        } catch (IOException e) {
            log.log(Level.SEVERE, "Error while creating flat index", e);
        }
    }

    @Override
    public List<Flat> searchFlat(String query) throws QueryNodeException {
        try {
            searcherManager.maybeRefresh();
            IndexSearcher acquire = searcherManager.acquire();
            StandardQueryParser parser = new StandardQueryParser(analyzer);
            parser.setDefaultOperator(StandardQueryConfigHandler.Operator.OR);
            Map<String, PointsConfig> map = new HashMap<>();
            map.put("rooms", new PointsConfig(new DecimalFormat(), Integer.class));
            map.put("metrage", new PointsConfig(new DecimalFormat(), Float.class));
            parser.setPointsConfigMap(map);
            Query parse = parser.parse(query, "city");
            TopDocs search = acquire.search(new BooleanQuery.Builder()
                            .add(parse, BooleanClause.Occur.MUST)
                            .add(new TermQuery(new Term("type", "flat")), BooleanClause.Occur.MUST)
                            .build(),
                    10);
            List<UUID> ids = Arrays.stream(search.scoreDocs).map(scoreDoc -> {
                        try {
                            return acquire.doc(scoreDoc.doc);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .map(flatDocument -> UUID.fromString(flatDocument.get("id"))).collect(Collectors.toList());
            return flatRepository.findAllById(ids);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createFlat(Flat flat) throws IOException {
        Document doc = toDocument(flat);

        indexWriter.addDocument(doc);
        indexWriter.commit();
    }

    @Override
    public void updateFlat(Flat flat) throws IOException {
        Document doc = toDocument(flat);

        indexWriter.updateDocument(new Term("id", flat.getId().toString()), doc);
        indexWriter.commit();
    }

    @Override
    public void deleteFlat(Flat flat) throws IOException {
        indexWriter.deleteDocuments(new Term("id", flat.getId().toString()));
        indexWriter.commit();
    }

    @Override
    public List<Post> searchPost(String query) {
        try {
            searcherManager.maybeRefresh();
            IndexSearcher acquire = searcherManager.acquire();
            StandardQueryParser parser = new StandardQueryParser(analyzer);
            parser.setDefaultOperator(StandardQueryConfigHandler.Operator.OR);
            Map<String, PointsConfig> map = new HashMap<>();
            map.put("price", new PointsConfig(new DecimalFormat(), Integer.class));
            Query parse = parser.parse(query, "price");
            TopDocs search = acquire.search(new BooleanQuery.Builder()
                            .add(parse, BooleanClause.Occur.MUST)
                            .add(new TermQuery(new Term("type", "post")), BooleanClause.Occur.MUST)
                            .build(),
                    10);
            List<UUID> ids = Arrays.stream(search.scoreDocs).map(scoreDoc -> {
                        try {
                            return acquire.doc(scoreDoc.doc);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .map(flatDocument -> UUID.fromString(flatDocument.get("id"))).collect(Collectors.toList());
            return postRepository.findAllById(ids);
        } catch (IOException | QueryNodeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createPost(Post post) throws IOException {
        Document doc = toDocument(post);

        indexWriter.addDocument(doc);
        indexWriter.commit();
    }

    @Override
    public void updatePost(Post post) throws IOException {
        Document doc = toDocument(post);

        indexWriter.updateDocument(new Term("id", post.getId().toString()), doc);
        indexWriter.commit();
    }

    @Override
    public void deletePost(Post post) throws IOException {
        indexWriter.deleteDocuments(new Term("id", post.getId().toString()));
        indexWriter.commit();
    }

    private void createFlatIndex(List<Flat> flats) throws IOException {
        List<Document> docs = new ArrayList<>(flats.size());
        for (Flat flat : flats) {
            Document doc = toDocument(flat);
            docs.add(doc);
        }
        indexWriter.addDocuments(docs);
    }

    private void createPostIndex(List<Post> all) throws IOException {
        List<Document> docs = new ArrayList<>(all.size());
        for (Post post : all) {
            Document doc = toDocument(post);
            docs.add(doc);
        }
        indexWriter.addDocuments(docs);
    }

    private Document toDocument(Flat flat) {
        Document doc = new Document();
        doc.add(new StringField("type", "flat", Field.Store.YES));
        doc.add(new StringField("id", flat.getId().toString(), Field.Store.YES));
        doc.add(new StringField("city", flat.getCity().toLowerCase(Locale.ROOT), Field.Store.YES));
        doc.add(new StringField("country", flat.getCountry().toLowerCase(Locale.ROOT), Field.Store.YES));
        doc.add(new TextField("address", flat.getAdress().toLowerCase(Locale.ROOT), Field.Store.YES));
        doc.add(new FloatPoint("metrage", flat.getMetrage()));
        doc.add(new IntPoint("rooms", flat.getNumOfRooms()));
        return doc;
    }

    private Document toDocument(Post post) {
        Document doc = new Document();
        doc.add(new StringField("type", "post", Field.Store.YES));
        doc.add(new StringField("id", post.getId().toString(), Field.Store.YES));
        doc.add(new TextField("title", post.getTitle().toLowerCase(Locale.ROOT), Field.Store.YES));
        doc.add(new TextField("description", post.getDescription().toLowerCase(Locale.ROOT), Field.Store.YES));
        doc.add(new IntPoint("price", post.getPrice()));
        return doc;
    }
}
