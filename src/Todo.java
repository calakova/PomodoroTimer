public class Todo {
    private final String name;
    private final int estimatedPomodoros;
    private int finishedPomodoros;

    public Todo(String name, int estimatedPomodoros) {
        this.name = name;
        this.estimatedPomodoros = Math.max(estimatedPomodoros, 1);
        this.finishedPomodoros = 0;
    }

    public String toString() {
        return name + ", " + finishedPomodoros + "/" + estimatedPomodoros;
    }

    public void incrementFinishedPomodoros() {
        finishedPomodoros++;
    }
}
