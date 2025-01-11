package racikanresep;

public class RacikanObat {
	private int idRacikanObat;
	private int idRacikan;
	private int idObat;
	private String dosis;
	private String frekuensi;

	public RacikanObat(int idRacikanObat, int idRacikan, int idObat, String dosis, String frekuensi) {
		this.idRacikanObat = idRacikanObat;
		this.idRacikan = idRacikan;
		this.idObat = idObat;
		this.dosis = dosis;
		this.frekuensi = frekuensi;
	}

	public int getIdRacikanObat() {
		return idRacikanObat;
	}

	public int getIdRacikan() {
		return idRacikan;
	}

	public int getIdObat() {
		return idObat;
	}

	public String getDosis() {
		return dosis;
	}

	public String getFrekuensi() {
		return frekuensi;
	}
}
