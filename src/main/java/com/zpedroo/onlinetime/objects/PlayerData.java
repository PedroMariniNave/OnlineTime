package com.zpedroo.onlinetime.objects;

import com.zpedroo.onlinetime.utils.config.Settings;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PlayerData {

    private UUID uuid;
    private Long loginTime;
    private Long playedTime;
    private Long firstJoinTime;
    private Long spentPoints;

    public PlayerData(UUID uuid, Long playedTime, Long firstJoinTime, Long spentPoints) {
        this.uuid = uuid;
        this.playedTime = playedTime;
        this.firstJoinTime = firstJoinTime;
        this.spentPoints = spentPoints;
    }

    public UUID getUUID() {
        return uuid;
    }

    public Long getLevel() {
        return getTotalOnlineTime() / TimeUnit.HOURS.toMillis(1L);
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public Long getPlayedTime() {
        return playedTime;
    }

    public Long getFirstJoinTime() {
        return firstJoinTime;
    }

    public Long getTotalOnlineTime() {
        if (loginTime == null) return playedTime;

        return playedTime + (System.currentTimeMillis() - loginTime);
    }

    public Long getSpentPoints() {
        return spentPoints;
    }

    public Long getPointsAmount() {
        double totalOnlineTime = (double) getTotalOnlineTime();
        double hourInMillis = (double) TimeUnit.HOURS.toMillis(1L);
        double pointsPerHour = (double) Settings.POINTS_PER_HOUR;

        // total online time (millis) - spent points (millis) / 1 hour in millis * points amount per hour
        double pointsAmount = ((totalOnlineTime - spentPoints) / hourInMillis) * pointsPerHour;
        if (pointsAmount < 0D) pointsAmount = 0D;

        return (long) pointsAmount;
    }

    public void addSpentPoints(Long spentPoints) {
        this.spentPoints += spentPoints;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }
}