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

package net.fabiopichler.simpleblogapiwithspringframework.service;

import net.fabiopichler.simpleblogapiwithspringframework.dto.PostCreationDto;
import net.fabiopichler.simpleblogapiwithspringframework.dto.PostDto;
import net.fabiopichler.simpleblogapiwithspringframework.model.Post;
import net.fabiopichler.simpleblogapiwithspringframework.model.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {

    Page<PostDto> findPaginated(int page, int size);

    Page<PostDto> findAllByUserPaginated(User user, int page, int size);

    Page<PostDto> searchPaginated(String body, int page, int size);

    PostDto create(PostCreationDto postDto, User user);

    Post findById(Long id);

    PostDto findByPostname(String postname);

    List<PostDto> findLastPosts();
}
