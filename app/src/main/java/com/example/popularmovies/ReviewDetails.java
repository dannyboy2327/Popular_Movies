package com.example.popularmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.popularmovies.databinding.ActivityReviewDetailsBinding;

public class ReviewDetails extends AppCompatActivity {

    private Intent reviewInfoIntent;
    private ActivityReviewDetailsBinding mReviewDetailsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mReviewDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_review_details);


        reviewInfoIntent = getIntent();

        if (reviewInfoIntent != null) {
            setReviewDetailsContent();
        }
    }

    private void setReviewDetailsContent() {
        String author = reviewInfoIntent.getStringExtra("Author");
        String content = reviewInfoIntent.getStringExtra("Content");
        String url = reviewInfoIntent.getStringExtra("Url");

        mReviewDetailsBinding.tvAuthor.setText(author);
        mReviewDetailsBinding.tvContent.setText(content);
        mReviewDetailsBinding.tvUrl.setText(url);
    }

}