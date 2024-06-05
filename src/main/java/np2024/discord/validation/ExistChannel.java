package np2024.discord.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExistChannelValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistChannel {

    String message() default "존재하지 않는 채널 ID입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
