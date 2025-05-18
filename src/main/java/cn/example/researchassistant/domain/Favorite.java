package cn.example.researchassistant.domain;

public class Favorite {
    private int Uid; // 用户ID
    private int Rid; // 资源ID
    private int flag; // 标志位：0表示文章，1表示视频

    public Favorite() {}

    public Favorite(int Uid, int Rid, int flag) {
        this.Uid = Uid;
        this.Rid = Rid;
        this.flag = flag;
    }

    // Getter 和 Setter 方法
    public int getUid() {
        return Uid;
    }

    public void setUid(int Uid) {
        this.Uid = Uid;
    }

    public int getRid() {
        return Rid;
    }

    public void setRid(int Rid) {
        this.Rid = Rid;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Favorite favorite = (Favorite) o;

        if (Uid != favorite.Uid) return false;
        if (Rid != favorite.Rid) return false;
        return flag == favorite.flag;
    }

    @Override
    public int hashCode() {
        int result = Uid;
        result = 31 * result + Rid;
        result = 31 * result + flag;
        return result;
    }
}