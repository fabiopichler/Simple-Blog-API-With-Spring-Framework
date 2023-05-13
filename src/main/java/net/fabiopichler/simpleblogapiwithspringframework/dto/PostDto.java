/*-------------------------------------------------------------------------------

Copyright (c) 2023 Fábio Pichler

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

-------------------------------------------------------------------------------*/

package net.fabiopichler.simpleblogapiwithspringframework.dto;

import net.fabiopichler.simpleblogapiwithspringframework.model.Post;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PostDto {

    private final Long id;
    private final UserDto user;
    private final List<CommentDto> comments;
    private final Long commentsCount;
    private final String postname;
    private final String type;
    private final String status;
    private final String commentStatus;
    private final String title;
    private final String description;
    private final String body;
    private final LocalDateTime createdAt;
    private final String createdAtFormatted;
    private final LocalDateTime updatedAt;
    private final String updatedAtFormatted;

    public PostDto(final Post post) {
        this(post, false);
    }

    public PostDto(final Post post, final boolean withComments) {
        this.id = post.getId();
        this.user = new UserDto(post.getUser());

        this.comments = withComments
                ? post.getComments().stream().map(CommentDto::new).collect(Collectors.toList())
                : new ArrayList<>();

        this.commentsCount = post.getCommentsCount();
        this.postname = post.getPostname();
        this.type = post.getType();
        this.status = post.getStatus();
        this.commentStatus = post.getCommentStatus();
        this.title = post.getTitle();
        this.description = post.getDescription();
        this.body = post.getBody();
        this.createdAt = post.getCreatedAt();
        this.createdAtFormatted = post.getCreatedAtFormatted();
        this.updatedAt = post.getUpdatedAt();
        this.updatedAtFormatted = post.getUpdatedAtFormatted();
    }

    public Long getId() {
        return id;
    }

    public UserDto getUser() {
        return user;
    }

    public List<CommentDto> getComments() {
        return comments;
    }

    public Long getCommentsCount() {
        return commentsCount;
    }

    public String getPostname() {
        return postname;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public String getCommentStatus() {
        return commentStatus;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getBody() {
        return body;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getCreatedAtFormatted() {
        return createdAtFormatted;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getUpdatedAtFormatted() {
        return updatedAtFormatted;
    }
}
