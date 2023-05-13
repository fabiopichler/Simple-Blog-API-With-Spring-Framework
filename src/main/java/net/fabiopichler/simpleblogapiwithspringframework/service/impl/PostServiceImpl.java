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

import com.github.slugify.Slugify;
import net.fabiopichler.simpleblogapiwithspringframework.dto.PostCreationDto;
import net.fabiopichler.simpleblogapiwithspringframework.dto.PostDto;
import net.fabiopichler.simpleblogapiwithspringframework.model.Post;
import net.fabiopichler.simpleblogapiwithspringframework.model.User;
import net.fabiopichler.simpleblogapiwithspringframework.repository.PostRepository;
import net.fabiopichler.simpleblogapiwithspringframework.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository repository;

    @Override
    public Page<PostDto> findPaginated(int page, int size) {
        return repository.findAll(getPageable(page, size)).map(PostDto::new);
    }

    @Override
    public Page<PostDto> findAllByUserPaginated(User user, int page, int size) {
        return repository.findAllByUser(user, getPageable(page, size)).map(PostDto::new);
    }

    @Override
    public Page<PostDto> searchPaginated(String body, int page, int size) {
        return repository.findByBodyContaining(body, getPageable(page, size)).map(PostDto::new);
    }

    @Override
    public PostDto create(PostCreationDto postDto, User user) {
        String postname = getPostnameSlug(postDto);

        if (repository.existsByPostname(postname))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Postname já existe");

        var post = new Post();

        post.setUser(user);
        post.setType("post");
        post.setStatus("publish");
        post.setPostname(postname);
        post.setTitle(postDto.getTitle());
        post.setBody(postDto.getBody());

        return new PostDto(repository.save(post));
    }

    @Override
    public Post findById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found")
        );
    }

    @Override
    public PostDto findByPostname(String postname) {
        return new PostDto(
                repository.findByPostname(postname).orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found")
                ),
                true
        );
    }

    @Override
    public List<PostDto> findLastPosts() {
        return repository.findFirst5AllByOrderByCreatedAtDesc().stream().map(PostDto::new).collect(Collectors.toList());
    }

    private Pageable getPageable(int page, int size) {
        return PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
    }

    private static String getPostnameSlug(PostCreationDto postDto) {
        String postname = postDto.getPostname();

        if (postname.isBlank())
            postname = postDto.getTitle();

        return Slugify.builder().build().slugify(postname);
    }
}
