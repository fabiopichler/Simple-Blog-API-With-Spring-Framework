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

package net.fabiopichler.simpleblogapiwithspringframework.service.impl;

import net.fabiopichler.simpleblogapiwithspringframework.dto.CommentCreationDto;
import net.fabiopichler.simpleblogapiwithspringframework.dto.CommentDto;
import net.fabiopichler.simpleblogapiwithspringframework.model.Comment;
import net.fabiopichler.simpleblogapiwithspringframework.model.Post;
import net.fabiopichler.simpleblogapiwithspringframework.model.User;
import net.fabiopichler.simpleblogapiwithspringframework.repository.CommentRepository;
import net.fabiopichler.simpleblogapiwithspringframework.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository repository;

    @Override
    public CommentDto create(CommentCreationDto commentDto, Post post, User user) {
        var comment = new Comment();

        comment.setPost(post);

        if (user != null) {
            comment.setUser(user);
            comment.setAuthorName(user.getName());
            comment.setAuthorEmail(user.getEmail());
        } else {
            comment.setAuthorName(commentDto.getAuthorName());
            comment.setAuthorEmail(commentDto.getAuthorEmail());
        }

        comment.setBody(commentDto.getBody());

        return new CommentDto(repository.save(comment));
    }
}
