package com.specificgroup.blog.util.mapper;

import com.specificgroup.blog.dto.response.PostResponse;
import com.specificgroup.blog.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "creationDate", source = "creationDate", dateFormat = "dd MMM yyyy, HH:mm")
    @Mapping(target = "modificationDate", source = "modificationDate", dateFormat = "dd MMM yyyy, HH:mm")
    @Mapping(target = "username", ignore = true)
    PostResponse postToPostResponse(Post post);
}
