package com.cnbcyln.app.akordefterim.util.SlideNav;


public interface SlidingRootNav {
    boolean isMenuHidden();

    boolean isMenuLocked();

    void closeMenu();

    void closeMenu(boolean animated);

    void openMenu();

    void openMenu(boolean animated);

    void setMenuLocked(boolean locked);

    SlidingRootNavLayout getLayout();
}
