package me.elmosahoe.mobslayerplugin;

import java.util.List;

public class Reward {
    private int xp;
    private int money;
    private List<String> items;

    public Reward(int xp, int money, List<String> items) {
        this.xp = xp;
        this.money = money;
        this.items = items;
    }

    public int getXp() {
        return xp;
    }

    public int getMoney() {
        return money;
    }

    public List<String> getItems() {
        return items;
    }
}
