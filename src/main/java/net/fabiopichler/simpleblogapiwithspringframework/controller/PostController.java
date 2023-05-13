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

package net.fabiopichler.simpleblogapiwithspringframework.controller;

import jakarta.validation.Valid;
import net.fabiopichler.simpleblogapiwithspringframework.dto.PostCreationDto;
import net.fabiopichler.simpleblogapiwithspringframework.dto.PostDto;
import net.fabiopichler.simpleblogapiwithspringframework.model.User;
import net.fabiopichler.simpleblogapiwithspringframework.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public Page<PostDto> home(@RequestParam(required = false) Optional<Integer> index) {
        return postService.findPaginated(index.orElse(1), 5);
    }

    @GetMapping("/search")
    public Page<PostDto> search(@RequestParam(required = false) Optional<Integer> index, @RequestParam String q) {
        return postService.searchPaginated(q, index.orElse(1), 5);
    }

    @GetMapping("/last")
    public List<PostDto> getLastPosts(@RequestParam(required = false) Optional<Integer> index) {
        return postService.findLastPosts();
    }

    @GetMapping("/{postname}")
    public PostDto show(@PathVariable String postname) {
        return postService.findByPostname(postname);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public PostDto store(@RequestBody @Valid PostCreationDto postDto, @AuthenticationPrincipal User user) {
        return postService.create(postDto, user);
    }
}
