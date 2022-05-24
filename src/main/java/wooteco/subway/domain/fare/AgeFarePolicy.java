package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Predicate;

public enum AgeFarePolicy {
    LESS_THAN_6(age -> age.lessThan(6)) {
        @Override
        public Fare apply(Fare fare, Age age) {
            return FREE;
        }
    },
    FROM_6_TO_13(age -> age.moreThan(6) && age.lessThan(13)) {
        @Override
        public Fare apply(Fare fare, Age age) {
            return fare.minus(350)
                    .discountPercent(50);
        }
    },
    FROM_13_TO_19(age -> age.moreThan(13) && age.lessThan(19)) {
        @Override
        public Fare apply(Fare fare, Age age) {
            return fare.minus(350)
                    .discountPercent(20);
        }
    },
    MORE_THAN_20(age -> age.moreThan(19)) {
        @Override
        public Fare apply(Fare fare, Age age) {
            return fare;
        }
    };

    private static final Fare FREE = new Fare(0);
    private final Predicate<Age> condition;

    AgeFarePolicy(Predicate<Age> condition) {
        this.condition = condition;
    }

    abstract public Fare apply(Fare fare, Age age);

    public static Fare getFare(Fare fare, Age age) {
        AgeFarePolicy farePolicy = findFarePolicy(age);
        return farePolicy.apply(fare, age);
    }

    private static AgeFarePolicy findFarePolicy(Age age) {
        return Arrays.stream(values())
                .filter(farePolicy -> farePolicy.condition.test(age))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("요금 정책을 찾을 수 없습니다."));
    }
}
