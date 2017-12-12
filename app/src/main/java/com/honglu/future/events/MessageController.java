package com.honglu.future.events;


public class MessageController {
    private static MessageController ourInstance = new MessageController();
    private FriendCountChange friendCountChange;
    private BeFocusedCountChange beFocusedCountChange;

    public static MessageController getInstance() {
        return ourInstance;
    }

    private MessageController() {
    }
    public interface FriendCountChange{
        void change();
    }

    public interface BeFocusedCountChange{
        void change();
    }

    public void setFriendCountChange(FriendCountChange friendCountChange) {
        this.friendCountChange = friendCountChange;
    }

    public FriendCountChange getFriendCountChange() {
        return friendCountChange;
    }
    public BeFocusedCountChange getBeFocusedCountChange() {
        return beFocusedCountChange;
    }

    public void setBeFocusedCountChange(BeFocusedCountChange beFocusedCountChange) {
        this.beFocusedCountChange = beFocusedCountChange;
    }
}
