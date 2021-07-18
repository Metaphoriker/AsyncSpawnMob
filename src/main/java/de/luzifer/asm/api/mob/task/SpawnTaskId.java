package de.luzifer.asm.api.mob.task;

import java.util.Objects;

public class SpawnTaskId {

    private final int TASK_ID;

    public static SpawnTaskId of(int id) {
        return new SpawnTaskId(id);
    }

    public SpawnTaskId(int taskId) {
        this.TASK_ID = taskId;
    }

    public int getTaskId() {
        return TASK_ID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpawnTaskId that = (SpawnTaskId) o;
        return TASK_ID == that.TASK_ID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(TASK_ID);
    }
}
