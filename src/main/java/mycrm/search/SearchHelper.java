package mycrm.search;

import org.springframework.stereotype.Component;

@Component
public class SearchHelper {
    public int getTotalPages(int pageSize, Long totalResults) {
        return pageSize == 0 ? 1 : (int) Math.ceil((double) totalResults / (double) pageSize);
    }
}
