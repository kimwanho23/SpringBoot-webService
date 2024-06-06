package kwh.awsweb.web;

import jakarta.servlet.http.HttpSession;
import kwh.awsweb.config.auth.dto.SessionUser;
import kwh.awsweb.service.posts.PostsService;
import kwh.awsweb.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
@Slf4j
public class IndexController {

    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("posts", postsService.findAllDesc());
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if (user != null) {
            log.info(user.getName());
            model.addAttribute("user", user.getName());
        }
        return "index";
    }

    @GetMapping("/posts/save")
    public String Postsaves() {
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String PostUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);
        return "posts-update";
    }
}
