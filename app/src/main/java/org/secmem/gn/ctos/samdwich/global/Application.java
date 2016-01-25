package org.secmem.gn.ctos.samdwich.global;

/**
 * Created by 김희중 on 2016-01-20.
 */
public final class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "DEFAULT", "SeoulNamsanB.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "SeoulNamsanB.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "SeoulNamsanB.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "SeoulNamsanB.ttf");
    }
}