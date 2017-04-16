package com.einstens3.ironchef.services;

import com.einstens3.ironchef.models.Challenge;
import com.parse.GetCallback;
import com.parse.ParseQuery;

import static com.parse.ParseQuery.getQuery;

public class ChallengeService {
    /**
     * Get Challenge by the challenge id
     */
    public static void getChallenge(String challengeID, final GetCallback<Challenge> callback) {
        ParseQuery<Challenge> query = getQuery(Challenge.class);
        query.getInBackground(challengeID, callback);
    }
}
