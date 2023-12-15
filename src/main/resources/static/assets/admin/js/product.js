app.controller("product-ctrl", function($scope, $filter, $http) {
	$scope.initialize = function() {
		$http.get("/rest/categorydetail").then(resp => {
			$scope.categories = resp.data;
		})
		$http.get("/rest/colors").then(resp => {
			$scope.colors = resp.data;
		})
		$http.get("/rest/description").then(resp => {
			$scope.description = resp.data;
		})

		$http.get("/rest/sizes").then(resp => {
			$scope.sizes = resp.data;
			console.log("SIZE", $scope.sizes);
		})
		$http.get("/rest/products").then(resp => {
			$scope.items = resp.data;
			console.log($scope.items);
			$scope.items.forEach(item => {
				item.dateInsert = new Date(item.dateInsert)
			})

			$scope.items.forEach(item => {
				item.dateUpdate = new Date(item.dateUpdate)
			})
		});

		$scope.reset();
	}


	$scope.reset = function() {
		$scope.productSize = [];
		$scope.form = {
			dateUpdate: new Date(),
			dateInsert: new Date(),
			sale: false,
			status: true,
			thumbnail: "cloud-upload.jpg"
		}
		$scope.displayedImages = [];
		$scope.closeCollapsibles();

	}

	$scope.clearImageProduct = () => {
		$scope.selectedFiles = [];
		const imageContainer = document.getElementById('imageContainer');
		imageContainer.innerHTML = '';
	}

	$scope.clearThumbnail = () => {
		$scope.image = []
		const imageContainer = document.getElementById('ImageThumbnail');
		imageContainer.innerHTML = '';
	}

	loadProductDetail = (id) =>{
		$http.get(`/rest/productsDetail/${id}`).then(resp => {
			$scope.productSize = []
			$scope.dataProductDetail =  resp.data;
			resp.data.forEach(function(value) {
				var sizeProduct = {
					id: value.size.id,
					name: value.size.name,
					quantity: value.quantity
				};
				$scope.productSize.push(sizeProduct);
			});
			loadSizeEdit();
		})
	}

	$scope.edit = function(item) {
		$scope.clearValidateForm();
		loadImageThumbnail(item);
		$scope.form = angular.copy(item);
		console.log("FORM: ", $scope.form);
		$(".nav-pills a:eq(0)").tab("show");
		loadProductDetail(item.id);
		$http.get(`/rest/image/${item.id}`).then(resp => {
			$scope.selectedFiles = resp.data;
			updateImageContainer();
		})
		$scope.selected = [];
		$scope.displayedImages = [];
	}

	loadImageDetail = (imageUrlArray) => {
		const imageContainer = document.getElementById('imageContainer');
		imageContainer.innerHTML = '';

		for (let i = 0; i < imageUrlArray.length; i++) {
			const img = document.createElement('img');
			img.src = imageUrlArray[i].image;
			img.alt = `Image ${i + 1}`;
			img.style.width = '100px';
			img.style.height = '100px';
			img.style.marginRight = '10px';
			img.style.marginBottom = '10px';
			imageContainer.appendChild(img);
		}
	}

	$scope.create = async () => {
		if (!validateForm()) {
			return;
		}
		if ($scope.form.thumbnail instanceof File) {
			await $scope.uploadImageThumbnail();
		}
		await $scope.createDescription();
		var item = angular.copy($scope.form);
		item.dateInsert = new Date()
		item.dateUpdate = new Date()
		await $http.post(`/rest/products`, item).then(async resp => {
			resp.data.dateInsert = new Date(resp.data.dateInsert)
			resp.data.dateUpdate = new Date(resp.data.dateUpdate)
			$scope.items.push(resp.data);
			$scope.form = angular.copy(resp.data);
			getValueSize();
			await $scope.uploadImageDetail();
			loadProductDetail($scope.form.id);
			// $scope.reset();
			// $scope.clearImageProduct();
			// $scope.clearThumbnail();
			alert("Thêm mới sản phẩm thành công!");
		}).catch(error => {
			alert("Lỗi thêm mới sản phẩm!");
			console.log("Error", error);
		});
	}

	$scope.update = async () => {
		if (!validateForm()) {
			return;
		}
		$scope.updateDescription();
		// var size = angular.copy($scope.productSize);
		await $scope.uploadImageDetail();
		if ($scope.form.thumbnail instanceof File) {
			await $scope.uploadImageThumbnail();
		}
		getInputsValues();
		console.log("arrProductDetail: ", $scope.arrProductDetail );
		for (var i = 0; i < $scope.arrProductDetail.length; i++) {
			$http.put(`/rest/productsDetail/${$scope.arrProductDetail[i].id}`, $scope.arrProductDetail[i]).then(resp => {
			}).catch(error => {
				alert("Lỗi cập nhật sảna phẩm!");
				console.log("Error", error);
			});
		}
		console.log("$scope.dataProductDetail", $scope.dataProductDetail)
		if ($scope.form.description.id == null) {
			$scope.form.description.id = $scope.oldDescriptionId;
		}
		var item = angular.copy($scope.form);
		item.dateUpdate = new Date();
		await $http.put(`/rest/products/${item.id}`, item).then(resp => {
			var index = $scope.items.findIndex(p => p.id == item.id);
			$scope.items[index] = item;
			$scope.oldDescriptionId = $scope.form.description.id;
			loadProductDetail(item.id);
			alert("Cập nhật sản phẩm thành công!!!!");
		}).catch(error => {
			alert("Lỗi cập nhật sảna phẩm!");
			console.log("Error", error);
		});
	}

	$scope.updateStatus = function(product) {
		var item = angular.copy(product);
		item.dateUpdate = new Date();
		if (item.status == false) {
			item.status = true;
		} else {
			item.status = false;
		}
		$http.put(`/rest/products/${item.id}`, item).then(resp => {
			var index = $scope.items.findIndex(p => p.id == item.id);
			$scope.items[index] = item;
			$scope.messege("Cập nhật trạng thái thành công");
		})
			.catch(error => {
				alert("Lỗi cập nhật!");
				console.log("Error", error);
			});
	}

	////anh thum
	InputPreviewImageThumbnail = (input) => {
		loadImageThumbnail(input.files);
	}

	function fillImageThumbnail(files) {
		const imageContainer = document.getElementById('ImageThumbnail');
		imageContainer.innerHTML = '';

		for (let i = 0; i < files.length; i++) {
			const img = document.createElement('img');
			img.src = URL.createObjectURL(files[i]);
			img.alt = files[i].name;
			img.style.width = '250px';
			img.style.height = '250px';
			img.style.marginRight = '10px';
			img.style.marginBottom = '10px';
			img.style.borderRadius = '20px';
			imageContainer.appendChild(img);
		}

		for (let i = 0; i < $scope.selectedFiles.length; i++) {
			const img = document.createElement('img');
			if ($scope.selectedFiles[i].name) {
				img.src = URL.createObjectURL($scope.selectedFiles[i]);
				img.alt = $scope.selectedFiles[i].name;
			} else if ($scope.selectedFiles[i].image) {
				img.src = $scope.selectedFiles[i].image;
				img.alt = `Image ${i + 1}`;
			}
			img.style.width = '100px';
			img.style.height = '100px';
			img.style.marginRight = '10px';
			img.style.marginBottom = '10px';
			img.style.borderRadius = '20px';
			img.addEventListener('dblclick', function() {
				deleteImage(i);
			});

			imageContainer.appendChild(img);
		}
	}

	loadImageThumbnail = (imageFile) => {
		$scope.image = imageFile;
		const imageContainer = document.getElementById('ImageThumbnail');
		imageContainer.innerHTML = '';
		const img = document.createElement('img');
		console.log("$scope.image", $scope.image);
		if ($scope.image instanceof FileList) {
			console.log("Đây là FileList:", image);
			for (let i = 0; i < $scope.image.length; i++) {
				img.src = URL.createObjectURL($scope.image[i]);
				console.log("SSSSSSSSSSSSSSS", $scope.image[i]);
				img.alt = $scope.image[i].name;
				img.style.width = '250px';
				img.style.height = '250px';
				img.style.marginRight = '10px';
				img.style.marginBottom = '10px';
				img.style.borderRadius = '20px';
				imageContainer.appendChild(img);
				$scope.form.thumbnail = $scope.image[i];
			}
		} else {
			console.log("Không phải là FileList:", $scope.image);
			img.src = $scope.image.thumbnail;
			img.style.width = '250px';
			img.style.height = '250px';
			img.style.marginRight = '10px';
			img.style.marginBottom = '10px';
			img.style.borderRadius = '20px';
			imageContainer.appendChild(img);
		}
	}

	/// upload xuong db anh thumbnail
	$scope.uploadImageThumbnail = async function() {
		var data = new FormData();
		console.log(" $scope.form.thumbnail[0]", $scope.form.thumbnail)
		data.append('uploadfile', $scope.form.thumbnail);
		console.log("data form", data)
		await $http.post('/rest/upload', data, {
			transformRequest: angular.identity,
			headers: { 'Content-Type': undefined }
		}).then(resp => {
			if (resp.status === 200) {
				$scope.form.thumbnail = resp.data.image;
				console.log("UPload thành công", $scope.form.thumbnail);
			}
		}).catch(error => {
			alert("Lỗi upload hình ảnh");
			console.log("Error", error);
		})
	}

	// upload xuong database anh phu
	$scope.uploadImageDetail = async function() {
		$scope.listImageProductCopy = $scope.selectedFiles;
		console.log(" $scope.selectedFiles", $scope.selectedFiles);
		var form = new FormData();
		for (var i = 0; i < $scope.selectedFiles.length; i++) {
			console.log(" $scope.selectedFiles[i]", $scope.selectedFiles[i]);
			if ($scope.selectedFiles[i] instanceof File) {
				form.append("uploadfiles", $scope.selectedFiles[i]);
			}
		}
		if (form.entries().next().done) {
			updateImageProduct();
		} else {
			await $http.post('/rest/uploadmulti', form, {
				transformRequest: angular.identity,
				headers: { 'Content-Type': undefined }
			}).then(resp => {
				if (resp.status === 200) {
					updateImageProduct(resp.data);
				}
			}).catch(error => {
				console.log("Errors", error);
			});
		}
	};

	updateImageProduct = async (listUrlImage) => {

		console.log("$scope.listImageProductCopy", $scope.listImageProductCopy)
		if ($scope.listImageProductCopy.length === 0) {
			console.log("$scope.listImageProductCopy", $scope.listImageProductCopy)
			await $http.delete(`/rest/image/delete-by-product${$scope.form.id}`).then(resp => {
				// $scope.image.push(resp.data);
			}).catch(error => {
				alert("Lỗi thêm hinh !");
				console.log("Error", error);
			});
			return;
		}

		var j = 0;
		for (var i = 0; i < $scope.selectedFiles.length; i++) {
			if ($scope.selectedFiles[i] instanceof File) {
				var objectImageProduct = {
					image: listUrlImage.images[j],
					product: {
						id: $scope.form.id
					}
				};
				$scope.selectedFiles[i] = objectImageProduct;
				j++;
			} else {
				var objectImageProduct = {
					id: $scope.selectedFiles[i].id,
					image: $scope.selectedFiles[i].image,
					product: {
						id: $scope.form.id
					}
				}
				$scope.selectedFiles[i] = objectImageProduct;
			}
		}
		console.log("images", $scope.selectedFiles);
		await $http.post(`/rest/image`, $scope.selectedFiles).then(resp => {
			// $scope.image.push(resp.data);
		}).catch(error => {
			alert("Lỗi thêm hinh !");
			console.log("Error", error);
		});
	}

	$scope.selectedFiles = [];
	uploadFiles1 = (input) => {
		const files = input.files;
		for (let i = 0; i < files.length; i++) {
			$scope.selectedFiles.push(files[i]);
		}
		updateImageContainer();
		console.log($scope.selectedFiles);
	}

	function updateImageContainer() {
		const imageContainer = document.getElementById('imageContainer');
		imageContainer.innerHTML = '';

		for (let i = 0; i < $scope.selectedFiles.length; i++) {
			const img = document.createElement('img');

			if ($scope.selectedFiles[i].name) {
				// File selected from user's device
				img.src = URL.createObjectURL($scope.selectedFiles[i]);
				img.alt = $scope.selectedFiles[i].name;
			} else if ($scope.selectedFiles[i].image) {
				// URL from cloud storage (e.g., Cloudinary)
				img.src = $scope.selectedFiles[i].image;
				img.alt = `Image ${i + 1}`;
			}

			img.style.width = '100px';
			img.style.height = '100px';
			img.style.marginRight = '10px';
			img.style.marginBottom = '10px';
			img.style.borderRadius = '20px';
			img.addEventListener('dblclick', function() {
				deleteImage(i);
			});

			imageContainer.appendChild(img);
		}
	}
	function updateImageContainerDuplicate() {
		const imageContainer = document.getElementById('imageContainer1');
		imageContainer.innerHTML = '';

		for (let i = 0; i < $scope.selectedFiles.length; i++) {
			const img = document.createElement('img');

			if ($scope.selectedFiles[i].name) {
				// File selected from user's device
				img.src = URL.createObjectURL($scope.selectedFiles[i]);
				img.alt = $scope.selectedFiles[i].name;
			} else if ($scope.selectedFiles[i].image) {
				// URL from cloud storage (e.g., Cloudinary)
				img.src = $scope.selectedFiles[i].image;
				img.alt = `Image ${i + 1}`;
			}

			img.style.width = '100px';
			img.style.height = '100px';
			img.style.marginRight = '10px';
			img.style.marginBottom = '10px';
			img.style.borderRadius = '20px';
			img.addEventListener('dblclick', function() {
				deleteImage(i);
			});

			imageContainer.appendChild(img);
		}
	}
	function deleteImage(index) {
		$scope.selectedFiles.splice(index, 1);
		updateImageContainer();
	}

	$scope.selected = [];

	$scope.exist = function(size) {
		return $scope.selected.indexOf(size) > -1;
	}

	$scope.toggleSelection = function(item) {
		var idx = $scope.selected.indexOf(item);
		if (idx > -1) {
			$scope.selected.splice(idx, 1);
		}
		else {
			$scope.selected.push(item);
		}
	}

	$scope.oldDescriptionId = null;
	$scope.descriptionDetail = function(selectedValue) {
		$http.get("/rest/description/" + selectedValue).then(function(response) {
			$scope.form.description = response.data;
		}).catch(function(error) {
			console.log("Error", error);
		});
	};


	$scope.createDescription = async function() {
		if (!validateFormDetail()) {
			return;
		}
		var item = angular.copy($scope.form.description);
		await $http.post(`/rest/description`, item).then(resp => {
			resp.data.dateInsert = new Date(resp.data.dateInsert)
			$scope.description.push(resp.data);
			$scope.form.description = resp.data;
			console.log($scope.form.description);
			// alert("Thêm mới mô tả sản phẩm thành công!");
		}).catch(error => {
			alert("Lỗi thêm mới !");
			console.log("Error", error);
		});
	}

	$scope.resetDescription = function() {
		$scope.oldDescriptionId = $scope.form.description.id;
		$scope.form.description = {
		}
	}

	$scope.updateDescription = function() {
		var item = angular.copy($scope.form.description);
		$http.put(`/rest/description/${item.id}`, item).then(resp => {
			var index = $scope.description.findIndex(p => p.id == item.id);
			$scope.description[index] = item;
			// alert("Cập nhật mô tả sản phẩm công!");
		})
			.catch(error => {
				alert("Lỗi cập nhật !");
				console.log("Error", error);
			});
	}

	$scope.closeCollapsibles = function() {
		// Close all collapsible elements
		var collapsibles = document.querySelectorAll('.collapse');
		for (var i = 0; i < collapsibles.length; i++) {
			var collapsible = collapsibles[i];
			if (collapsible.classList.contains('show')) {
				var collapseInstance = new bootstrap.Collapse(collapsible);
				collapseInstance.hide();
			}
		}
	};


	$scope.messege = (mes) => {
		$.toast({
			text: mes, // Text that is to be shown in the toast
			heading: 'Thông báo', // Optional heading to be shown on the toast
			icon: 'success', // Type of toast icon
			showHideTransition: 'fade', // fade, slide or plain
			allowToastClose: true, // Boolean value true or false
			hideAfter: 2000, // false to make it sticky or number representing the miliseconds as time after which toast needs to be hidden
			stack: 5, // false if there should be only one toast at a time or a number representing the maximum number of toasts to be shown at a time
			position: 'top-right', // bottom-left or bottom-right or bottom-center or top-left or top-right or top-center or mid-center or an object representing the left, right, top, bottom values
			textAlign: 'left',  // Text alignment i.e. left, right or center
			loader: true,  // Whether to show loader or not. True by default
			loaderBg: '#9EC600',  // Background color of the toast loader
			beforeShow: function() { }, // will be triggered before the toast is shown
			afterShown: function() { }, // will be triggered after the toat has been shown
			beforeHide: function() { }, // will be triggered before the toast gets hidden
			afterHidden: function() { }  // will be triggered after the toast has been hidden
		});
	}

	$scope.initialize();
	$scope.searchText = {};
	$scope.items = [];
	$scope.pager = {
		page: 0,
		size: 10,
		sortColumn: 'item.id',
		sortDirection: 'desc',
		get filteredItems() {
			var filteredItems = $filter('filter')($scope.items, $scope.searchText);
			if ($scope.pager.sortColumn === 'item.id') {
				filteredItems = $filter('orderBy')(filteredItems, 'item.id', $scope.pager.sortDirection === 'asc');
			}
			return filteredItems;
		},
		get items() {
			if (this.page < 0) {
				this.last();
			}
			if (this.page >= this.count) {
				this.first();
			}
			var filteredItems = this.filteredItems;
			if ($scope.pager.sortColumn) {
				filteredItems = $filter('orderBy')(filteredItems, $scope.pager.sortColumn, $scope.pager.sortDirection == 'desc');
			}
			var start = this.page * this.size;
			return filteredItems.slice(start, start + this.size)
		},
		get count() {
			return Math.ceil(1.0 * this.filteredItems.length / this.size);
		},
		first() {
			this.page = 0;
		},
		last() {
			this.page = this.count - 1;
		},
		next() {
			this.page++;
		},
		prev() {
			this.page--;
		}
	}
	$scope.toggleSort = function(column) {
		if ($scope.pager.sortColumn == column) {
			$scope.pager.sortDirection = ($scope.pager.sortDirection == 'asc') ? 'desc' : 'asc';
		} else {
			$scope.pager.sortColumn = column;
			$scope.pager.sortDirection = 'asc';
		}
	};
	$scope.getSortIconClass = function(column) {
		if ($scope.pager.sortColumn == column) {
			return 'sort-icon ' + ($scope.pager.sortDirection == 'asc' ? 'asc' : 'desc');
		}
	};

	$scope.isSortedBy = function(column) {
		return $scope.pager.sortColumn == column;
	};


	$scope.$watch('form.description.id', function(newValue) {
		if (newValue) {
			$scope.descriptionDetail(newValue);
		}

	});


	validateForm = () => {

		var isvalid = true;

		var name = document.getElementById("productName").value
		var price = document.getElementById("productPrice").value
		var weight = document.getElementById("weight").value
		var discountPrice = document.getElementById("ProductDiscountPrice").value
		var productcolor = document.getElementById("productColor")
		var productcaterory = document.getElementById("productCategory")
		var productdescription = document.getElementById("productDescription")
		var selectedTextproductColor = productcolor.options[productcolor.selectedIndex].text;
		var selectedTextproductCategory = productcaterory.options[productcaterory.selectedIndex].text;
		var selectedTextproductDes = productdescription.options[productdescription.selectedIndex].text;




		console.log(selectedTextproductColor)
		console.log(selectedTextproductCategory)
		console.log(selectedTextproductDes)
		if (name === "") {
			isvalid = false;
			document.getElementById("productNameError").innerText = "Tên sản phẩm không bỏ trống";
		} else {
			document.getElementById("productNameError").innerText = "";
		}

		if (weight === '') {
			isvalid = false;
			document.getElementById("weightError").innerText = "Không được để trống cân nặng";
		} else {
			document.getElementById("weightError").innerText = "";
		}

		if (price === "") {
			isvalid = false;
			document.getElementById("productPriceError").innerText = "Giá không bỏ trống";
		} else if (/[!@#$%^&*(),.?":{}|<>]/.test(price)) {
			// Kiểm tra xem giá có phải là số hay không
			isvalid = false;
			document.getElementById("productPriceError").innerText = "Giá không được chứa ký tự đặt biệt";
		} else if (isNaN(price)) {
			// Kiểm tra xem giá có chứa ký tự đặc biệt hay không
			isvalid = false;
			document.getElementById("productPriceError").innerText = "Giá phải là một số";
		} else if (parseFloat(price) <= 0) {
			// Kiểm tra xem giá có lớn hơn 0 hay không
			isvalid = false;
			document.getElementById("productPriceError").innerText = "Giá phải lớn hơn 0";
		} else {
			document.getElementById("productPriceError").innerText = "";
		}

		if (discountPrice === "") {
			isvalid = false;
			document.getElementById("ProductDiscountPriceError").innerText = "Số tiền giảm không bỏ trống";
		} else if (/[!@#$%^&*(),.?":{}|<>]/.test(discountPrice)) {
			// Kiểm tra xem giá có chứa ký tự đặc biệt ở bất kỳ vị trí không
			isvalid = false;
			document.getElementById("ProductDiscountPriceError").innerText = "Số tiền không được chứa ký tự đặc biệt";
		} else if (isNaN(discountPrice)) {
			// Kiểm tra xem giá có phải là số hay không
			isvalid = false;
			document.getElementById("ProductDiscountPriceError").innerText = "Số tiền phải là một số";
		} else if (parseFloat(discountPrice) <= 0) {
			// Kiểm tra xem giá có lớn hơn 0 hay không
			isvalid = false;
			document.getElementById("ProductDiscountPriceError").innerText = "Số tiền phải lớn hơn 0";
		} else if (parseFloat(discountPrice) > parseFloat(price)) {
			// Kiểm tra xem số tiền giảm có lớn hơn giá gốc không
			isvalid = false;
			document.getElementById("ProductDiscountPriceError").innerText = "Số tiền giảm phải nhỏ hơn giá gốc";
		} else {
			document.getElementById("ProductDiscountPriceError").innerText = "";
		}


		if (selectedTextproductColor == "") {
			isvalid = false;
			document.getElementById("productColorError").innerText = "Vui lòng chọn màu sản phẩm";
		} else {
			document.getElementById("productColorError").innerText = "";
		}

		if (selectedTextproductCategory == "") {
			isvalid = false;
			document.getElementById("productCategoryError").innerText = "Vui lòng chọn loại sản phẩm";
		} else {
			document.getElementById("productCategoryError").innerText = "";
		}
		var descriptionname = document.getElementById("descriptionName").value
		var descriptionweight = document.getElementById("descriptionWeight").value
		var descriptionmaterial = document.getElementById("descriptionMaterial").value
		var descriptiontechnology = document.getElementById("descriptionTechnology").value
		var descriptionmanufacture = document.getElementById("descriptionManufacture").value
		var descriptiondescription = document.getElementById("descriptionDescription").value

		if (descriptionname === "") {
			isvalid = false;
			document.getElementById("descriptionNameError").innerText = "Tên chi tiết không bỏ trống";
		} else {
			document.getElementById("descriptionNameError").innerText = "";
		}

		if (descriptionweight === "") {
			isvalid = false;
			document.getElementById("descriptionWeightError").innerText = "Cân nặng không bỏ trống";
		}
		else {
			document.getElementById("descriptionWeightError").innerText = "";
		}

		if (descriptionmaterial === "") {
			isvalid = false;
			document.getElementById("descriptionMaterialError").innerText = "Chất liệu không bỏ trống";
		} else {
			document.getElementById("descriptionMaterialError").innerText = "";
		}

		if (descriptiontechnology === "") {
			isvalid = false;
			document.getElementById("descriptionTechnologyError").innerText = "Công nghệ không bỏ trống";
		} else {
			document.getElementById("descriptionTechnologyError").innerText = "";
		}

		if (descriptionmanufacture === "") {
			isvalid = false;
			document.getElementById("descriptionManufactureError").innerText = "Sản xuất không bỏ trống";
		} else {
			document.getElementById("descriptionManufactureError").innerText = "";
		}

		if (descriptiondescription === "") {
			isvalid = false;
			document.getElementById("descriptionDescriptionError").innerText = "Mô tả không bỏ trống";
		} else {
			document.getElementById("descriptionDescriptionError").innerText = "";
		}

		return isvalid;
	}


	validateFormDetail = () => {
		var isvalid = true;

		var descriptionname = document.getElementById("descriptionName").value
		var descriptionweight = document.getElementById("descriptionWeight").value
		var descriptionmaterial = document.getElementById("descriptionMaterial").value
		var descriptiontechnology = document.getElementById("descriptionTechnology").value
		var descriptionmanufacture = document.getElementById("descriptionManufacture").value
		var descriptiondescription = document.getElementById("descriptionDescription").value


		if (descriptionname === "") {
			isvalid = false;
			document.getElementById("descriptionNameError").innerText = "Không bỏ trống";
		} else {
			document.getElementById("descriptionNameError").innerText = "";
		}

		if (descriptionweight === "") {
			isvalid = false;
			document.getElementById("descriptionWeightError").innerText = "Không bỏ trống";
		}
		else {
			document.getElementById("descriptionWeightError").innerText = "";
		}

		if (descriptionmaterial === "") {
			isvalid = false;
			document.getElementById("descriptionMaterialError").innerText = "Không bỏ trống";
		} else {
			document.getElementById("descriptionMaterialError").innerText = "";
		}

		if (descriptiontechnology === "") {
			isvalid = false;
			document.getElementById("descriptionTechnologyError").innerText = "Không bỏ trống";
		} else {
			document.getElementById("descriptionTechnologyError").innerText = "";
		}

		if (descriptionmanufacture === "") {
			isvalid = false;
			document.getElementById("descriptionManufactureError").innerText = "Không bỏ trống";
		} else {
			document.getElementById("descriptionManufactureError").innerText = "";
		}

		if (descriptiondescription === "") {
			isvalid = false;
			document.getElementById("descriptionDescriptionError").innerText = "Không bỏ trống";
		} else {
			document.getElementById("descriptionDescriptionError").innerText = "";
		}
		return isvalid;
	}

	toggleInput = (checkbox) => {
		var index = checkbox.getAttribute('data-size-index');
		var input = document.querySelector('.input-quantity-size[data-size-index="' + index + '"]');

		if (checkbox.checked) {
			input.style.display = 'inline-block'; // Show the input
			input.value = 0; // Set input value to 0
		} else {
			input.style.display = 'none'; // Hide the input
		}
	}

	getValueSize =  () => {
		var inputs = document.querySelectorAll('.input-quantity-size');
		$scope.datasize = [];
		inputs.forEach(function (input) {
			if (input.style.display !== 'none') {
				var index = input.getAttribute('data-size-index');
				var name = input.getAttribute('data-size-name');
				$scope.datasize.push({
					id: parseInt(index),
					name: name,
					quantity: parseInt(input.value)
				});
			}
		});
		console.log("productdetail", $scope.datasize)
		$scope.datasize.forEach(function(value) {
			var productDetail = {
				quantity : value.quantity,
				size :{
					id: value.id,
					name: value.name
				},
				product :{
					id: $scope.form.id
				}
			}
			console.log("productdetail", productDetail)
			$http.post(`/rest/productsDetail`, productDetail).then(resp => {
				console.log("data tra ve", resp.data);
				var sizeProduct = {
					id: resp.data.size.id,
					name: resp.data.size.name,
					quantity: resp.data.quantity
				};
				$scope.productSize.push(sizeProduct);
				loadSizeEdit();
			}).catch(error => {
				alert("Lỗi thêm size sản phẩm!");
				console.log("Error", error);
			});
		});
	}

	loadSizeEdit = () =>{
		console.log("size1",$scope.productSize);
		$scope.sizeEdit = $scope.sizes.filter(item2 => !$scope.productSize.some(item1 => item1.id === item2.id));
		console.log("size", $scope.sizeEdit)
		$scope.datasize = [];
	}

	$scope.addsize = () => {
		var checkboxes = document.querySelectorAll('.checkbox-size-product');
		var selectedSizes = [];

		for (var i = 0; i < checkboxes.length; i++) {
			var checkbox = checkboxes[i];

			if (checkbox.checked) {
				var sizeIndex = checkbox.getAttribute('data-size-index');
				var index = $scope.sizeEdit.findIndex(function (size) {
					return size.id == sizeIndex;
				});

				if (index !== -1) {
					var sizeProduct = {
						id: $scope.sizeEdit[index].id,
						name: $scope.sizeEdit[index].name,
						quantity: 0
					};
					$scope.productSize.push(sizeProduct);
					selectedSizes.push($scope.sizeEdit[index]);
					$scope.sizeEdit.splice(index, 1);
				}
			}
		}
		console.log(selectedSizes);
	};

	getInputsValues = () => {
		var inputs = document.querySelectorAll('.input-size');
		$scope.productSize  =[]
		$scope.arrProductDetail  =	[]
		// console.log("Trước khi thay: ",$scope.dataProductDetail);
		for (var i = 0; i < inputs.length; i++) {
			var input = inputs[i];
			var id = input.getAttribute('data-size-index');
			var name = input.getAttribute('data-size-name');
			var quantity = parseInt(input.value);
			console.log("dataProductDetail",$scope.dataProductDetail[i])
			var productId = $scope.dataProductDetail[i] && $scope.dataProductDetail[i].id ? $scope.dataProductDetail[i].id : 0;
			console.log("productID", productId)
			var objectProductDetail = {
				id: productId,
				product:{
					id: $scope.form.id,
				},
				quantity: quantity,
				size:{
					id:parseInt(id)
				}
			}
			$scope.productSize.push({
				id:parseInt(id),
				name: name,
				quantity: quantity
			});
			$scope.arrProductDetail.push(objectProductDetail);
		}
		console.log("Mảng productDetail: ",$scope.arrProductDetail);
	}

	$scope.clearValidateForm = () =>{

		document.getElementById("productNameError").innerText = "";
		document.getElementById("weightError").innerText = "";
		document.getElementById("productPriceError").innerText = "";
		document.getElementById("productPriceError").innerText = "";
		document.getElementById("productPriceError").innerText = "";
		document.getElementById("ProductDiscountPriceError").innerText = "";
		document.getElementById("productColorError").innerText = "";
		document.getElementById("productCategoryError").innerText = "";
		document.getElementById("descriptionNameError").innerText = "";
		document.getElementById("descriptionWeightError").innerText = "";
		document.getElementById("descriptionMaterialError").innerText = "";
		document.getElementById("descriptionTechnologyError").innerText = "";
		document.getElementById("descriptionManufactureError").innerText = "";
		document.getElementById("descriptionDescriptionError").innerText = "";
	}
	preventNegative =(event) => {
		const inputElement = event.target;
		const inputValue = inputElement.value;

		const sanitizedValue = inputValue.replace(/^-/, '');

		inputElement.value = sanitizedValue;
	}
});