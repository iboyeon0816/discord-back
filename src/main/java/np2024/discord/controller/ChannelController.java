package np2024.discord.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import np2024.discord.domain.Channel;
import np2024.discord.dto.ChannelRequestDto.CreateDto;
import np2024.discord.dto.ChannelRequestDto.UpdateDto;
import np2024.discord.dto.ChannelResponseDto.CreateResultDto;
import np2024.discord.dto.ChannelResponseDto.GetResultDto;
import np2024.discord.repository.ChannelRepository;
import np2024.discord.repository.SessionRepository;
import np2024.discord.validation.ExistChannel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/channels")
@RequiredArgsConstructor
@Tag(name = "Channels", description = "채팅 채널 CRUD API")
public class ChannelController {

    private final ChannelRepository channelRepository;
    private final SessionRepository sessionRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "채팅 채널 생성 API", description = "채널 이름과 사용자 이름을 받아 채널을 생성하는 API입니다. " +
            "채널이 정상적으로 생성되었을 경우 생성된 채널의 ID를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "채널이 생성되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 형식의 요청입니다. 필수 필드의 값이 주어지지 않았거나 존재하지 않는 사용자 이름일 수 있습니다.")
    })
    public CreateResultDto create(@RequestBody(description = "생성할 채널의 이름 (필수 필드, 공백 불가)\n생성하는 사용자의 이름 (필수 필드, 공백 불가)", required = true)
    @org.springframework.web.bind.annotation.RequestBody @Valid CreateDto request){
        validateExistingUser(request);
        Channel channel = channelRepository.save(request);
        return new CreateResultDto(channel.getId());
    }

    @GetMapping
    @Operation(summary = "전체 채팅 채널 조회 API", description = "전체 채팅 채널을 조회할 수 있는 API입니다. " +
            "조회 성공 시 모든 채널의 ID와 채널명, 채널 생성자 이름을 리스트 형식으로 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청이 정상적으로 수행되었습니다."),
    })
    public List<GetResultDto> getAll() {
        return channelRepository.findAll().stream()
                .map(channel -> new GetResultDto(channel.getId(), channel.getName(), channel.getCreatorName()))
                .collect(Collectors.toList());
    }

    @PutMapping("/{channelId}")
    @Operation(summary = "채팅 채널명 변경 API", description = "사용자로부터 변경할 채널의 ID와 이름을 받아 채널명을 변경하는 API입니다. ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "채널명이 변경되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 형식의 요청입니다. " +
                    "필수 필드의 값이 주어지지 않았거나 존재하지 않는 채널 ID일 수 있습니다.")
    })
    @Parameters({
            @Parameter(name = "channelId", description = "변경할 채널의 ID (path variable, 필수 필드)"),
    })
    public void update(@PathVariable @ExistChannel Long channelId,
                       @RequestBody(description = "변경할 채널명 (필수 필드, 공백 불가)", required = true)
                       @org.springframework.web.bind.annotation.RequestBody @Valid UpdateDto request) {
        channelRepository.update(channelId, request);
    }

    @DeleteMapping("/{channelId}")
    @Operation(summary = "채팅 채널 삭제 API", description = "사용자로부터 채널의 ID를 받아 채널을 삭제하는 API입니다. ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "채널이 삭제되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 형식의 요청입니다. " +
                    "필수 필드의 값이 주어지지 않았거나 존재하지 않는 채널 ID일 수 있습니다.")
    })
    @Parameters({
            @Parameter(name = "channelId", description = "삭제할 채널의 ID (path variable, 필수 필드)"),
    })
    public void delete(@PathVariable @ExistChannel Long channelId) {
        channelRepository.delete(channelId);
    }

    private void validateExistingUser(CreateDto request) {
        if (!sessionRepository.exists(request.getUsername())) {
            throw new IllegalArgumentException("존재하지 않는 사용자 이름입니다.");
        }
    }
}
