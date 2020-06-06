package com.example.bloodwallet;


public class ScoredPostList implements Comparable<ScoredPostList> {
    public Post post;
    public Double score;
    public Integer percent;

    public ScoredPostList(){

    }

    public ScoredPostList(Post post, Double score, Double percent){
        this.post = post;
        this.score = score;
        this.percent = (int)(100 * percent);
    }

    @Override
    public int compareTo(ScoredPostList scoredPostList) {
        return this.score.compareTo(scoredPostList.score);
    }


}
