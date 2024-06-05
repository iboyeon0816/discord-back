package np2024.discord.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import np2024.discord.dto.UserResponseDto.GetResultDto;
import np2024.discord.repository.SessionRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "서버와 연결된 사용자 정보 API")
public class UserController {

    private final SessionRepository sessionRepository;

    @GetMapping
    @Operation(summary = "온라인 상태의 사용자 조회 API", description = "서버와 연결되어 있는 모든 사용자 정보를 조회할 수 있는 API입니다. " +
            "조회 성공 시 모든 사용자의 이름을 리스트 형식으로 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청이 정상적으로 수행되었습니다."),
    })
    public List<GetResultDto> getAll() {
        return sessionRepository.findAll().stream()
                .map(user -> new GetResultDto(user.getName()))
                .collect(Collectors.toList());
    }
}
