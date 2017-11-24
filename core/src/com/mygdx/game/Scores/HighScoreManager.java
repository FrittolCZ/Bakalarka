package com.mygdx.game.Scores;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class HighScoreManager {

    private ArrayList<HighScore> scores;

    private FileHandle highScoreFile;

    private Json json = new Json();

    public HighScoreManager() {
        highScoreFile = Gdx.files.internal("highscores.json");
        //highScoreFile.delete();
        scores = new ArrayList<HighScore>();
        if (highScoreFile.exists()) {
            loadScoresFromFile();
        }
    }

    /**
     * Loads records from json file
     *
     * @return List of all high scores
     */
    private void loadScoresFromFile() {
        try {
            String text = highScoreFile.readString();
            JsonValue data = new JsonReader().parse(text);
            int dataSize = data.size;
            for (int i = 0; i < dataSize; i++) {
                String name = data.get(i).getString("playerName");
                String score = data.get(i).getString("score");
                String gameMode = data.get(i).getString("gameMode");
                String diffLvl = data.get(i).getString("diffLvl");
                scores.add(new HighScore(score, diffLvl, gameMode, name));
            }
        } catch (Exception e) {
            Gdx.app.log("JsonErr", "chyba", e);
        }
    }


    private void updateScoreFile() {
        String scoresString = json.toJson(scores);

        try {
            highScoreFile.writeString(scoresString, false);
        }
        catch (Exception e)
        {
            Gdx.app.log("SaveErr", "error", e);
        }
    }

    /**
     * Creates new record in json file
     *
     * @param score
     */
    public void storeNewScore(HighScore score) {
        scores.add(score);
        updateScoreFile();
    }

    public ArrayList<HighScore> getScores(String mod, String diffLvl) {
        ArrayList<HighScore> filtered = new ArrayList<HighScore>();
        for (HighScore h : scores) {
            if (h.getDiffLvl().equals(diffLvl) && h.getGameMode().equals(mod)) {
                filtered.add(h);
            }
        }
        Collections.sort(filtered, new Comparator<HighScore>() {
            public int compare(HighScore score1, HighScore score2) {
                return Integer.parseInt(score2.getScore()) - Integer.parseInt(score1.getScore());
            }
        });
        return filtered.size() > 5 ? new ArrayList<HighScore>(filtered.subList(0,4)) : filtered;
    }
}
