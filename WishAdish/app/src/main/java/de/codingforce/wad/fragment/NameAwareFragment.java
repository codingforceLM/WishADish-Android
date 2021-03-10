package de.codingforce.wad.fragment;

import androidx.fragment.app.Fragment;

/**
 * Baseclass for Fragments to force the implementation of a fragmentname
 */
public class NameAwareFragment extends Fragment{
    private String fragmentname = this.getClass().getSimpleName();

    /**s
     * Returns fragmentname
     * @return fragmentname
     */
    public String getFragmentname() {
        return fragmentname;
    }

    /**
     * Sets the fragmentname
     * @param name name
     */
    protected void setFragmentname(String name) {
        this.fragmentname = name;
    }
}
