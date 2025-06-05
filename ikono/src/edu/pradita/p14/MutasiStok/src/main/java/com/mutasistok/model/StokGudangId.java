package com.mutasistok.model;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class StokGudangId implements Serializable {

    private Long barangId;
    private int gudangId;

    public StokGudangId() {}

    // --- GETTER, SETTER, EQUALS, HASHCODE YANG DITAMBAHKAN ---
    public Long getBarangId() {
        return barangId;
    }

    public void setBarangId(Long barangId) {
        this.barangId = barangId;
    }

    public int getGudangId() {
        return gudangId;
    }

    public void setGudangId(int gudangId) {
        this.gudangId = gudangId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StokGudangId that = (StokGudangId) o;
        return gudangId == that.gudangId && Objects.equals(barangId, that.barangId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(barangId, gudangId);
    }
}