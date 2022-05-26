package com.zpedroo.onlinetime.objects;

import com.zpedroo.onlinetime.utils.config.Settings;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PlayerData {

    private final UUID uuid;
    private long loginTime = -1;
    private final long playedTime;
    private final long firstJoinTime;
    private long spentPoints;

    public PlayerData(UUID uuid, long playedTime, long firstJoinTime, long spentPoints) {
        this.uuid = uuid;
        this.playedTime = playedTime;
        this.firstJoinTime = firstJoinTime;
        this.spentPoints = spentPoints;
    }

    public UUID getUUID() {
        return uuid;
    }

    public long getLevel() {
        return getTotalOnlineTime() / TimeUnit.HOURS.toMillis(1L);
    }

    public long getLoginTime() {
        return loginTime;
    }

    public long getPlayedTime() {
        return playedTime;
    }

    public long getFirstJoinTime() {
        return firstJoinTime;
    }

    public long getTotalOnlineTime() {
        if (loginTime == -1) return playedTime;

        return playedTime + (System.currentTimeMillis() - loginTime);
    }

    public long getSpentPoints() {
        return spentPoints;
    }

    public long getPointsAmount() {
        double totalOnlineTime = (double) getTotalOnlineTime();
        double hourInMillis = (double) TimeUnit.HOURS.toMillis(1L);
        double pointsPerHour = (double) Settings.POINTS_PER_HOUR;

        // total online time (millis) - spent points (millis) / 1 hour in millis * points amount per hour
        double pointsAmount = ((totalOnlineTime - spentPoints) / hourInMillis) * pointsPerHour;
        if (pointsAmount < 0D) pointsAmount = 0D;

        return (long) pointsAmount;
    }

    public void addSpentPoints(long spentPoints) {
        this.spentPoints += spentPoints;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }
}