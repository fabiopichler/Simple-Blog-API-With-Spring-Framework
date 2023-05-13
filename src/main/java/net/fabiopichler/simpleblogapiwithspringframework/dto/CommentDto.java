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

import net.fabiopichler.simpleblogapiwithspringframework.model.Comment;
import net.fabiopichler.simpleblogapiwithspringframework.model.User;

import java.time.LocalDateTime;

public class CommentDto {

    private final Long id;
    private final String authorName;
    private final String body;
    private final UserDto user;
    private final LocalDateTime createdAt;
    private final String createdAtFormatted;

    public CommentDto(final Comment comment) {
        this.id = comment.getId();
        this.authorName = comment.getAuthorName();
        this.body = comment.getBody();

        final User user = comment.getUser();
        this.user = (user != null) ? new UserDto(user) : null;

        this.createdAt = comment.getCreatedAt();
        this.createdAtFormatted = comment.getCreatedAtFormatted();
    }

    public Long getId() {
        return id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getBody() {
        return body;
    }

    public UserDto getUser() {
        return user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getCreatedAtFormatted() {
        return createdAtFormatted;
    }
}
