package com.fanok.mdpu24v1;

import java.util.ArrayList;

public class Para {
    private ArrayList<Integer> para = new ArrayList<>();

    public ArrayList<Integer> getPara() {
        return para;
    }

    public void setPara(Integer para) {
        this.para.add(para);
    }

    public boolean conteins(int a) {
        for (int i = 0; i < para.size(); i++) {
            if (para.get(i) == a) return true;
        }
        return false;
    }
}
