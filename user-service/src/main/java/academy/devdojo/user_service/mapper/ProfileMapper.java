package academy.devdojo.user_service.mapper;

import academy.devdojo.user_service.domain.Profile;
import academy.devdojo.user_service.request.ProfilePostRequest;
import academy.devdojo.user_service.request.ProfilePostResponse;
import academy.devdojo.user_service.response.ProfileGetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileMapper INSTANCE = Mappers.getMapper(ProfileMapper.class);

    Profile toProfile(ProfilePostRequest request);

    ProfilePostResponse toProfilePostResponse(Profile profile);

    ProfileGetResponse toProfileGetResponse(Profile profile);

    List<ProfileGetResponse> toProfileGetResponseList(List<Profile> profiles);
}
