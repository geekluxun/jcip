package com.geekluxun;

/**
 * UnsafeStates
 * <p/>
 * Allowing internal mutable state to escape
 *
 * @author Brian Goetz and Tim Peierls
 */
class UnsafeStates {
    private String[] states = new String[]{
        "AK", "AL" /*...*/
    };

    /**
     * 把私有域states也暴露出去了，产生了逸出
     *
     * @return
     */
    public String[] getStates() {
        return states;
    }
}
