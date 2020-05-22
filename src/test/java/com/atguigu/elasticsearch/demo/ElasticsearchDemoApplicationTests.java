package com.atguigu.elasticsearch.demo;

import com.atguigu.elasticsearch.demo.pojo.User;
import com.atguigu.elasticsearch.demo.repository.UserRepository;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class ElasticsearchDemoApplicationTests {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private UserRepository userRepository;

    @Test
    void contextLoads() {
        this.elasticsearchRestTemplate.createIndex(User.class);
//		this.elasticsearchRestTemplate.deleteIndex(User.class);

    }

    @Test
    void add() {
//        this.userRepository.save(new User(2L, "green", 21, "123456"));
//        this.userRepository.save(new User(3L, "red", 22, "123456"));
//        this.userRepository.save(new User(4L, "blue", 23, "123456"));
//        this.userRepository.save(new User(5L, "yellow", 24, "123456"));
//        this.userRepository.save(new User(6L, "black", 26, "123456"));
//        List<User> userList = new ArrayList<>();
//        userList.add(new User(7L, "asd", 21, "123456"));
//        userList.add(new User(8L, "fgh", 22, "123456"));
//        userList.add(new User(9L, "qwer", 23, "123456"));
        List<User> userList = Arrays.asList(
                new User(1L, "star", 20, "123456"),
                new User(2L, "green", 21, "123456"),
                new User(3L, "red", 22, "123456"),
                new User(4L, "blue", 23, "123456"),
                new User(5L, "yellow", 24, "123456"),
                new User(6L, "black", 26, "123456")
        );
        userRepository.saveAll(userList);
    }

    @Test
    void testFind() {
//        System.out.println(this.userRepository.findAll());
    }

    @Test
    void findByAgeBetween() {
        this.userRepository.findByAgeBetween(20,22).forEach(System.out::println);
        this.userRepository.findByNameLike("f").forEach(System.out::println);
    }

    @Test
    void testNativeQuery() {
        //自定义查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //构建查询
        queryBuilder.withQuery(QueryBuilders.matchQuery("age", "22"));
        //构建分页条件
//        queryBuilder.withPageable(PageRequest.of(2, 2));
        //构建排序条件
        queryBuilder.withSort(SortBuilders.fieldSort("age").order(SortOrder.DESC));
        //高亮
        queryBuilder.withHighlightBuilder(new HighlightBuilder().field("name").preTags("<em>").postTags("</em>"));
        //执行查询
//        Page<User> userPage = this.userRepository.search(queryBuilder.build());
        Page<User> userPage = this.elasticsearchRestTemplate.queryForPage(queryBuilder.build(), User.class);
        userPage.forEach(System.out::println);

    }
}
