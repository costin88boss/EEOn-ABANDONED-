package com.costin.eeon.game.sounds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {

    private static SoundManager instance;

    public final Sound banned = Gdx.audio.newSound(Gdx.files.internal("sounds/banned.mp3"));
    public final Sound dontPanic = Gdx.audio.newSound(Gdx.files.internal("sounds/dontpanic.mp3"));

    public SoundManager() {
        instance = this;

    }

    public static SoundManager getInstance() {
        return instance;
    }

    private void generateSounds() {
        //lol?
    }
}
