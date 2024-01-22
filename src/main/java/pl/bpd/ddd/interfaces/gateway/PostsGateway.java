package pl.bpd.ddd.interfaces.gateway;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "placeholderApi", url = "https://jsonplaceholder.typicode.com")
public interface PostsGateway {
    @GetMapping("/posts")
    List<PostDto> getPosts();
}
