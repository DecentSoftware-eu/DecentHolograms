package eu.decentsoftware.holograms.api.utils.reflect;

public enum Version {
	v1_7_R1(1),
	v1_7_R2(2),
	v1_7_R3(3),
	v1_7_R4(4),
	v1_8_R1(5),
	v1_8_R2(6),
	v1_8_R3(7),
	v1_9_R1(8),
	v1_9_R2(9),
	v1_10_R1(10),
	v1_11_R1(11),
	v1_12_R1(12),
	v1_13_R1(13),
	v1_13_R2(14),
	v1_14_R1(15),
	v1_15_R1(16),
	v1_16_R1(17),
	v1_16_R2(18),
	v1_16_R3(19),
	v1_17_R1(20);

	private final int id;

	Version(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public boolean isAfter(Version version) {
		return this.getId() > version.getId();
	}

	public boolean isAfterOrEqual(Version version) {
		return this.getId() >= version.getId();
	}

	public boolean isBefore(Version version) {
		return this.getId() < version.getId();
	}

	public boolean isBeforeOrEqual(Version version) {
		return this.getId() <= version.getId();
	}
}
