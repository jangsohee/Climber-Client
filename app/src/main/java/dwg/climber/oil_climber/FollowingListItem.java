package dwg.climber.oil_climber;


public class FollowingListItem {
    private Integer celebID;
    private String celebName;

    public FollowingListItem(Integer celebID, String celebName) {
        this.celebID = celebID;
        this.celebName = celebName;
    }

    public Integer getCelebID() {
        return this.celebID;
    }

    public void setCelebID(Integer id) {
        this.celebID = id;
    }

    public String getCelebName() {
        return this.celebName;
    }

    public void setCelebName(String name) {
        this.celebName = name;
    }
}
