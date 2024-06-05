package np2024.discord.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import np2024.discord.repository.ChannelRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistChannelValidator implements ConstraintValidator<ExistChannel, Long> {

    private final ChannelRepository channelRepository;

    @Override
    public boolean isValid(Long channelId, ConstraintValidatorContext context) {
        return channelRepository.exists(channelId);
    }
}
