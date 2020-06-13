
public class DungeonRun {

	Long time, gold, exp;
	String prettyTime;
	int mor, glory, index, refined, quartz;
	public DungeonRun(Long time, String prettyTime, Long gold, Long exp, int mor, int glory, int quartz, int refined) {
		super();
		this.time = time;
		this.prettyTime = prettyTime;
		this.gold = gold;
		this.exp = exp;
		this.mor = mor;
		this.glory = glory;
		index = 0;
		this.quartz = quartz;
		this.refined = refined;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public String getPrettyTime() {
		return prettyTime;
	}
	public void setPrettyTime(String prettyTime) {
		this.prettyTime = prettyTime;
	}
	public Long getGold() {
		return gold;
	}
	public void setGold(Long gold) {
		this.gold = gold;
	}
	public Long getExp() {
		return exp;
	}
	public void setExp(Long exp) {
		this.exp = exp;
	}
	public int getMor() {
		return mor;
	}
	public void setMor(int mor) {
		this.mor = mor;
	}
	public int getGlory() {
		return glory;
	}
	public void setGlory(int glory) {
		this.glory = glory;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getRefined() {
		return refined;
	}
	public void setRefined(int refined) {
		this.refined = refined;
	}
	public int getQuartz() {
		return quartz;
	}
	public void setQuartz(int quartz) {
		this.quartz = quartz;
	}
	
	
	
	
}
