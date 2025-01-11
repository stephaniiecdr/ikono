package racikanresep;

public class racikan {
	private int idRacikan;
	private String namaRacikan;
	private String deskripsi;

	public racikan(int idRacikan, String namaRacikan, String deskripsi) {
		this.idRacikan = idRacikan;
		this.namaRacikan = namaRacikan;
		this.deskripsi = deskripsi;
	}

	public int getIdRacikan() {
		return idRacikan;
	}

	public String getNamaRacikan() {
		return namaRacikan;
	}

	public String getDeskripsi() {
		return deskripsi;
	}
}
