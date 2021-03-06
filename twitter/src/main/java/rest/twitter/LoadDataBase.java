package rest.twitter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rest.twitter.controller.FollowTableController;
import rest.twitter.repository.*;
import rest.twitter.domian.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.*;

@Configuration
@Slf4j
public class LoadDataBase {

    @Bean
    CommandLineRunner initializeDataBase(UserRepository repository,FollowTableRepository repositoryFollowTable,TweetRepository tweetRepository, LikeTableRepository likeTableRepository,CommmetRepository commmetRepository){
        return args -> {
            //add users
            String[] name=new String[]{"William","Jason","Eric","Liam"};
            for(String first:name){
                    log.info("Preloading " + repository.save(new User(first, "Hello! I am "+ first+" !","123456")));
            }

            String[] tweets=new String[]{"You have no idea how much my heart races when I see you.",
                    "Since the time I've met you, I cry a little less, laugh a little harder and smile all the more, just because I have you, my life is a better place.",
                    "Every day with you is a wonderful addition to my life's journey.",
                    "Just when I think that it is impossible to love you any more than I already do, you prove me wrong.",
                    "I’m having one of those days that make me realize how lost I’d be without you.",
                    "Your cute smile can melt even the icy heart, which I have had before I met you. Thanks for all the happiness that you gave me, I love you.",
                    "I will always have this piece of my heart that smiles whenever I think about you.",
                    "My heart for you will never break. My smile for you will never fade. My love for you will never end. I love you!",
                    "Only one single phrase makes my heart beat faster – it is your name and the word forever.",
                    "When you need someone to be there for you, I’ll be right there by your side always!",
                    "Sometimes I wonder if love is worth fighting for. Then I look at you. I’m ready for war.",
                    "I want to be your favorite hello, and hardest goodbye.",
                    "Explaining to you how much and why I love you, would be like me describing how water tastes. It’s impossible.",
                    " I only saw you for a second, but it made my day.",
                    "I miss my sleep in the night and I miss the light in my day, It’s a wonderful feel to melt in love and I melt at your love!",
                    "Every love story is beautiful … but ours is my favorite!"};
            String[] comment=new String[]{
                    "Your are awsome!",
                    "Come on! You can do it!",
                    "Where are we frome? Where are we going?",
                    "What's the meaning of existance of life?"
            };
            // add follow and tweets randomly
            Set<String> set = new HashSet<>();
            Random rand=new Random();
            for(int i=0;i<4;i++){
                for(int j=0;j<4;j++) {
                    String temp = "" + i + " " + j;
                    if (!set.contains(temp) && i != j) {
                        FollowTable table=new FollowTable(i+1,j+1);
                        User user = repository.findById(table.getFollowedId()).get();//.orElseThrow(()->new UserNotFoundException(table.getFollowedId()));
                        user.setFollowers(user.getFollowers()+1);
                        repository.save(user);

                        //User with followerId in table increase following number by 1;
                        User user2 = repository.findById(table.getFollowerId()).get();//.orElseThrow(()->new UserNotFoundException(table.getFollowedId()));
                        user2.setFollowings(user.getFollowings()+1);
                        repository.save(user2);

                        repositoryFollowTable.save(table);
                        set.add(temp);
                    }
                    User user = repository.findById((long)i+1).get();
                    user.setTweets(user.getTweets() + 1);
                    repository.save(user);
                    log.info("preLoading" + tweetRepository.save(new Tweet(i+1, tweets[4*i+j],name[i])));
                }
                Tweet tweet = tweetRepository.findById((long)1).get();
                tweet.setComments(tweet.getComments()+1);
                tweetRepository.save(tweet);

                commmetRepository.save(new CommentTable(i+1,1,comment[i],name[i]));
            }

            // add like randomly
            Set<String> likeSet = new HashSet<>();
            for(int i=0;i<10;i++){
                int author=rand.nextInt(4)+1;
                int tweetId=rand.nextInt(16)+1;
                String temp=""+author+" "+tweetId;
                if(!likeSet.contains(temp)) {

                    Tweet tweet = tweetRepository.findById((long)tweetId).get();
                    tweet.setLikes(tweet.getLikes()+1);
                    tweetRepository.save(tweet);
                    log.info("PreLoading" + likeTableRepository.save(new LikeTable(author, tweetId)));
                    likeSet.add(temp);
                }
            }
        };
    }

}

