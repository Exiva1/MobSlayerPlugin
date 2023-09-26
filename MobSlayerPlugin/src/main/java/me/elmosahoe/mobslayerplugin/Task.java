package me.elmosahoe.mobslayerplugin;

public class Task {

    private final String mobType;
    private final int targetAmount;
    private int currentAmount;
    private Reward reward;

    public Task(String mobType, int targetAmount, Reward reward) {
        this.mobType = mobType;
        this.targetAmount = targetAmount;
        this.currentAmount = 0;
        this.reward = reward;
    }

    public String getMobType() {
        return mobType;
    }

    public int getTargetAmount() {
        return targetAmount;
    }

    public int getCurrentAmount() {
        return currentAmount;
    }

    public void incrementProgress() {
        currentAmount++;
    }

    public boolean isComplete() {
        return currentAmount >= targetAmount;
    }

    public Reward getReward() {
        return reward;
    }
}