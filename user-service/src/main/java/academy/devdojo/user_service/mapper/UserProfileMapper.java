package academy.devdojo.user_service.mapper;


import academy.devdojo.user_service.domain.User;
import academy.devdojo.user_service.domain.UserProfile;
import academy.devdojo.user_service.request.UserPostRequest;
import academy.devdojo.user_service.request.UserPutRequest;
import academy.devdojo.user_service.response.UserGetResponse;
import academy.devdojo.user_service.response.UserPostResponse;
import academy.devdojo.user_service.response.UserProfileGetResponse;
import academy.devdojo.user_service.response.UserProfileUserGetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfileMapper INSTANCE = Mappers.getMapper(UserProfileMapper.class);

    List<UserProfileGetResponse> toUserProfileGetResponse(List<UserProfile> userProfiles);
    List<UserProfileUserGetResponse> toUserProfileUserGetResponse(List<User> users);




}
