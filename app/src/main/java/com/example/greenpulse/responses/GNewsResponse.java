package com.example.greenpulse.responses;

import java.util.ArrayList;
import java.util.Date;

public class GNewsResponse {

    public int totalArticles;
    public ArrayList<Article> articles;
    public class Article{
        public String title;
        public String description;
        public String content;
        public String url;
        public String image;
        public Date publishedAt;
        public Source source;
    }
    public class Source{
        public String name;
        public String url;
    }

}
