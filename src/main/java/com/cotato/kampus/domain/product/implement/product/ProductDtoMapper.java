package com.cotato.kampus.domain.product.implement.product;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.product.domain.Product;
import com.cotato.kampus.domain.product.domain.ProductScrap;
import com.cotato.kampus.domain.product.domain.ProductThumbnail;
import com.cotato.kampus.domain.product.implement.productPhoto.ProductPhotoFinder;
import com.cotato.kampus.domain.product.implement.productScrap.ProductScrapFinder;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductDtoMapper {

	private final ProductPhotoFinder productPhotoFinder;
	private final ProductScrapFinder productScrapFinder;

	public ProductThumbnail toProductThumbnail(Product product, Long userId) {
		String thumbnailPhotoUrl = productPhotoFinder.findFirstPhoto(product.getId());
		boolean isScrapped = productScrapFinder.isScrapped(product.getId(), userId);
		return ProductThumbnail.from(product, thumbnailPhotoUrl, isScrapped);
	}

	public Slice<ProductThumbnail> toProductThumbnails(Slice<Product> products, Long userId) {
		return products.map(product -> toProductThumbnail(product, userId));
	}

	public ProductThumbnail toScrapProductThumbnail(Product product) {
		String thumbnailPhotoUrl = productPhotoFinder.findFirstPhoto(product.getId());
		return ProductThumbnail.from(product, thumbnailPhotoUrl, true);
	}

	public Slice<ProductThumbnail> toScrapProductThumbnails(Slice<ProductScrap> productScraps, List<Product> products) {
		Map<Long, Product> productMap = products.stream()
			.collect(Collectors.toMap(Product::getId, product -> product));

		List<ProductThumbnail> productThumbnails = productScraps.stream()
			.map(scrap -> {
				Long productId = scrap.getProductId();
				Product product = productMap.get(productId);
				return toScrapProductThumbnail(product);
			}).toList();

		return new SliceImpl<>(productThumbnails, productScraps.getPageable(), productScraps.hasNext());
	}
}
