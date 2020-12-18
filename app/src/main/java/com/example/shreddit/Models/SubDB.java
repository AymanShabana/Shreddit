package com.example.shreddit.Models;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Board.class}, version = 1)
public abstract class SubDB extends RoomDatabase {

    public abstract SubDao subDao();
    private static SubDB INSTANCE;
    public static SubDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (SubDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            SubDB.class, "board_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final SubDao mDao;
//        String[] ids = {"1", "2", "3","4","5","6","7","8","9","10"};
//        String[] titles = {"[Charania] Breaking: Giannis Antetokounmpo says he is signing a contract extension with the Milwaukee Bucks. The two-time MVP will sign a five-year, $228.2 million supermax extension with the franchise, the largest deal in NBA history, sources tell @TheAthleticNBA @Stadium.",
//                "TIL Frank Sinatra died the night of Seinfeld's finale and his ambulance made it to the hospital in record time because traffic was so light due to everyone watching the show.",
//                "Christmas 100+ Steam Key Giveaway!",
//                "Among Us is coming to Nintendo Switch later today",
//                "What’s the worst scandal to happen at your school?",
//                "I will never get bored of crocodiles just floating around",
//                "\"Are you going to pay my rent ? says owner Anton Van Happen of 'Nick The Greek' to public health inspectors issuing him a citation for staying open after being issued a closure order.",
//                "Los Angeles Covid-19 Update: Ambulances Waiting 4 Hours To Offload Patients As L.A. Has Just 56 Adult ICU Beds Left, Orange County Has None",
//                "One Piece chapter 999 spoilers",
//                "Negative stereotypes toward Black men can be provoked or deflected by clothing. Clothing emits signals of power and status and can carry with it certain stereotypes. Men in formal clothing were rated more trustworthy, intelligent, and warm than men in soccer uniforms."};
//        String[] boards = {"nba","todayilearned","Steam","NintendoSwitch","AskReddit","gifs","PublicFreakout","news","OnePiece","science"};
//        String[] boardIcons= {"https://styles.redditmedia.com/t5_2qo4s/styles/communityIcon_1podsfdai4301.png","","","","","","","","",""};
//        String[] postImgs ={"default","https://a.thumbs.redditmedia.com/u-RLCLhK4anLxSeSFsjri9c-Y0xJPXDVaoF-S6jrUp8.jpg","self", "https://a.thumbs.redditmedia.com/U8Q9Mot4m2zWUUlqgyUN1JCSTYWqGiNXM5xxagWbge0.jpg","self","https://b.thumbs.redditmedia.com/jB4GLY4zRrq9kIwD8wy0WDVw9IB8fyVj9owJGD-JK_k.jpg"
//                ,"https://b.thumbs.redditmedia.com/S58-MWQSJ9-PVtZcwfPB5uCu0LX0FqAPABiyYM-LIrw.jpg","default","spoiler", "https://b.thumbs.redditmedia.com/lX3su1zE2NpGujcpsLiWn7XVIL1PlBFkzOgIONAvSKA.jpg"};
//        String[] selfTexts ={"","","The Christmas season is upon us, as such **I have about 100 (112 at the time of counting) steam keys that I need to get rid of** ranging anywhere from a couple dollars to $90 AUD.\\n\\nI have decided that since my steam stats indicate Ive only opened  something like 4% of my games, its clear I don't need them. Instead I  came to the decision that I would give them away at random to try and  spread some fun during a time when its needed most.\\n\\nAs such I will be **giving away a random Steam key to 10 random users from this sub**. All you need to do is **comment to enter the giveaway**. Depending on the popularity of the post I may increase the amount of keys distributed but it will be 10 at least!\\n\\nMy **goal here is to give as many of these away as I can** as quite frankly I don't need them and I want to avoid dribbling them  out when I know so many people are needing a distraction right now.\\n\\nIn addition I will be hosting a stream on Twitch at **8PM ACST on Friday 18/12** in which all viewers are invited to join a game of marbles where **several entrants will win random steam keys**! *(If you aren't familiar with marbles but are interested I guarantee it is incredibly simple and requires nothing but your presence!*\\n\\nPlease join me at Twitch to join in: [www.twitch.tv/thestrange\\\\_one](https://www.twitch.tv/thestrange_one)\\n\\n**Important:** I want to be very clear in saying that you are not required or expected to follow/cheer/sub in any way whatsoever. Everyone has the same chance. I would go as far as to say I would advise you to avoid following unless you genuinely like the content or my personality as this isn't me trying to farm follows. I just wanted to do something good in a time its needed most. \\n\\n*This is also my second time running something like this as I've found myself in a pretty fortunate situation during the pandemic and wanted to do what I can to spread some positivity and distractions so if you've seen something similar by me before you know that this is genuine. This will make my total keys given away this year well over 300 which has been incredibly fun to do!*",
//                "","","","","","[Korean source](https://gall.dcinside.com/mgallery/board/view/?id=onepieceblood&amp;no=621140&amp;page=1)\\n\\nFrom Redon-\\n\\nBrief summary-\\n\\n*-* **Title: \\\"The waiting liquor I made for you\\\" (君がため醸みし待酒)**\\n\\n*- Yamato Vs. Ace.*\\n\\n*- When Ace was in Onigashima, Kaido was on an expedition.*\\n\\n*- Ace came to Onigashima to rescue children kidnapped from the mainland.*\\n\\n*- Ace says that parents couldn't choose, Yamato respond saying that she wanted to go out to the sea and live freely like Oden adventure*\\n\\n*.- After the fight, they get close to each other, drink together, and talk about the young pirates of the sea, including Luffy.*\\n\\n*- In the present, Marco tries to fly to the roof with Zoro.*\\n\\n*- Marco briefly recalls Ace.- Queen and King transform and try to stop Marco.*\\n\\n*- Tama learns that Luffy is Ace's brother.*\\n\\n*- Yamato mentions that Luffy has a \\\"D\\\" in his name.*\\n\\n*- Big Mom tells Kaidou to keep Nico Robin alive.*\\n\\n*- Kaido asks Big Mom about the girl with 3 eyes (Pudding) on her side, if she cannot read ancient texts yet.*\\n\\n*- Kaidou fruit is \\\"Uo Uo no Mi (Fish Fish Fruit), Mythical type\\\" (no more information about the exact model).*\\n\\n*- Big Mom gave it to Kaidou the day Rocks fell.*\\n\\n[Snippet from the final page](https://preview.redd.it/oqni4biycc561.png?width=672&amp;format=png&amp;auto=webp&amp;s=f65b4d77c77916f62dd101a6ee144a9cbbea9cc9)\\n\\n**JUMP** **BREAK NEXT WEEK**\\n\\n(But the scans and spoilers for Ch. 1000 will leak around a week before its official release which is slated for 4/1)\\n\\n*Detailed summary and raws on Thursday*",
//                ""};
//        int[] upvotes= {17307,42272,6396,19118,25413,35185,27404,50868,4046,32214};
//        int[] comments= {2946,773,17087,1116,10242,536,4086,3892,3543,3366};
//        long[] created= {1608056491,1608053233,1608039147,1608052650,1608046247,1608049429,1608036472,1608034023,1608033938,1608026740};
//        String[] links = {"https://twitter.com/shamscharania/status/1338912082578853888", "https://groovyhistory.com/frank-sinatra-death-seinfeld-finale",
//                "https://www.reddit.com/r/Steam/comments/kdls51/christmas_100_steam_key_giveaway/","https://twitter.com/NintendoEurope/status/1338895914631647233",
//                "https://www.reddit.com/r/AskReddit/comments/kdns7n/whats_the_worst_scandal_to_happen_at_your_school/","https://gfycat.com/quaintnaivefallowdeer",
//                "https://v.redd.it/a29viqdgkc561","https://deadline.com/2020/12/los-angeles-covid-19-l-a-just-56-adult-icu-beds-left-orange-county-0-icu-1234656346/",
//                "https://www.reddit.com/r/OnePiece/comments/kdkjrf/one_piece_chapter_999_spoilers/",
//                "https://www.psypost.org/2020/12/negative-stereotypes-toward-black-men-can-be-provoked-or-deflected-by-clothing-study-finds-58795"};
//        String[] authors = {"curryybacon","sunghooter","TheStrangeOne17","harushiga","Lost-Warning-2588","ADarkcid","ElGalloBjj","JHopeHoe","gyrozepp95","mvea"};
//        String[] types ={"link","link","text","link","text","image","video","link","text","link"};
        public PopulateDbAsync(SubDB db) {
            mDao = db.subDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mDao.deleteAll();

//            for (int i = 0; i <= ids.length - 1; i++) {
//                Post post = new Post(ids[i],titles[i],boards[i],boardIcons[i],postImgs[i],selfTexts[i],upvotes[i],
//                        comments[i],created[i],links[i],authors[i],types[i]);
//                mDao.insert(post);
//            }
            return null;
        }
    }
}
