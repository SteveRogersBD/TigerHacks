package com.example.greenpulse.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class YoutubeVideo {
    public SearchMetadata search_metadata;
    public SearchParameters search_parameters;
    public SearchInformation search_information;
    public ArrayList<VideoResult> video_results;
    public ArrayList<ShortsResult> shorts_results;
    public Pagination pagination;
    public SerpapiPagination serpapi_pagination;
    public class Channel{
        public String name;
        public String link;
        public boolean verified;
        public String thumbnail;
    }

    public class Pagination{
        public String current;
        public String next;
        public String next_page_token;
    }
    public class SearchInformation{
        public int total_results;
        public String video_results_state;
    }

    public class SearchMetadata{
        public String id;
        public String status;
        public String json_endpoint;
        public String created_at;
        public String processed_at;
        public String youtube_url;
        public String raw_html_file;
        public double total_time_taken;
    }

    public class SearchParameters{
        public String engine;
        public String search_query;
        public String gl;
        public String hl;
    }

    public class SerpapiPagination{
        public String current;
        public String next;
        public String next_page_token;
    }

    public class Short{
        public String title;
        public String link;
        public String thumbnail;
        public String views_original;
        public int views;
        public String video_id;
    }

    public class ShortsResult{
        public int position_on_page;
        public ArrayList<Short> shorts;
    }

    public class Thumbnail{
        @JsonProperty("static")
        public String statics;
        public String rich;
    }

    public class VideoResult{
        public int position_on_page;
        public String title;
        public String link;
        public String serpapi_link;
        public Channel channel;
        public String published_date;
        public int views;
        public String length;
        public String description;
        public Thumbnail thumbnail;
        public ArrayList<String> extensions;
    }

}
