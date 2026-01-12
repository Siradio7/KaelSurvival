package com.java_game_project.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

import java.util.HashMap;
import java.util.Map;

public class AudioManager {
    private static AudioManager audioManager;
    private final Map<String, Music> musics = new HashMap<>();
    private final Map<String, Sound> sounds = new HashMap<>();
    private Music currentMusic;
    private float musicVolume = 0.2f;
    private float soundVolume = 1f;

    public static AudioManager getInstance() {
        if (audioManager == null) {
            audioManager = new AudioManager();
        }

        return audioManager;
    }

    private AudioManager() {
    }

    public void loadMusic(String key, String filePath) {
        Music music = Gdx.audio.newMusic(Gdx.files.internal(filePath));

        musics.put(key, music);
    }

    public void loadMusics(String path) {
        FileHandle folder = Gdx.files.internal(path);

        if (!folder.exists() || !folder.isDirectory()) {
            folder = Gdx.files.internal("assets/" + path);
        }

        if (!folder.exists() || !folder.isDirectory()) {
            Gdx.app.error("AudioManager",
                    "Le dossier " + path + " (ou assets/" + path + ") est introuvable ou invalide !");
            return;
        }

        for (FileHandle file : folder.list()) {
            if (file.extension().equalsIgnoreCase("mp3") || file.extension().equalsIgnoreCase("ogg")) {
                String key = file.nameWithoutExtension();
                Music music = Gdx.audio.newMusic(file);

                musics.put(key, music);
            }
        }
    }

    public void loadSound(String key, String filePath) {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal(filePath));

        sounds.put(key, sound);
    }

    public void loadSounds(String path) {
        FileHandle folder = Gdx.files.internal(path);

        if (!folder.exists() || !folder.isDirectory()) {
            folder = Gdx.files.internal("assets/" + path);
        }

        if (!folder.exists() || !folder.isDirectory()) {
            Gdx.app.error("AudioManager",
                    "Le dossier " + path + " (ou assets/" + path + ") est introuvable ou invalide !");
            return;
        }

        for (FileHandle file : folder.list()) {
            if (file.extension().equalsIgnoreCase("wav") || file.extension().equalsIgnoreCase("ogg")) {
                String key = file.nameWithoutExtension();
                Sound sound = Gdx.audio.newSound(file);

                sounds.put(key, sound);
            }
        }
    }

    public void playMusic(String key, boolean loop) {
        stopMusic();
        currentMusic = musics.get(key);

        if (currentMusic != null) {
            currentMusic.setLooping(loop);
            currentMusic.setVolume(musicVolume);
            currentMusic.play();
        }
    }

    public void stopMusic() {
        if (currentMusic != null && currentMusic.isPlaying()) {
            currentMusic.stop();
        }
    }

    public void pauseMusic() {
        if (currentMusic != null && currentMusic.isPlaying()) {
            currentMusic.pause();
        }
    }

    public void resumeMusic() {
        if (currentMusic != null && !currentMusic.isPlaying()) {
            currentMusic.play();
        }
    }

    public void playSound(String key) {
        Sound sound = sounds.get(key);

        if (sound != null) {
            sound.play(soundVolume);
        }
    }

    public void setMusicVolume(float volume) {
        this.musicVolume = volume;

        if (currentMusic != null) {
            currentMusic.setVolume(volume);
        }
    }

    public void setSoundVolume(float volume) {
        this.soundVolume = volume;
    }

    public float getMusicVolume() {
        return musicVolume;
    }

    public float getSoundVolume() {
        return soundVolume;
    }

    public void dispose() {
        for (Music m : musics.values()) {
            m.dispose();
        }

        for (Sound s : sounds.values()) {
            s.dispose();
        }

        musics.clear();
        sounds.clear();
    }
}
