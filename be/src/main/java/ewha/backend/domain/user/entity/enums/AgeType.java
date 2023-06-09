package ewha.backend.domain.user.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum AgeType {
	TEENAGER(),
	TWENTIES(),
	THIRTIES(),
	FORTIES(),
	FIFTIES(),
	SIXTIES(),
	SEVENTIES(),
	EIGHTIES(),
	OTHERS();

	@JsonCreator
	public static AgeType from(String ageType) {
		return AgeType.valueOf(ageType.toUpperCase());
	}
}
