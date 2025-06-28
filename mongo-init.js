db.createCollection('categories', {capped: false})
db.createCollection('stores', {capped: false})
db.createCollection('users', {capped: false})
db.createCollection('deals', {capped: false})

db.categories.insertMany([
  {
    names: {en: 'Computers', tr: 'Bilgisayar'},
    parent: '/',
    category: '/computers',
    iconLigature: 'computer_baseline',
    iconFontFamily: 'MaterialIcons',
    isTag: false,
    _class: 'category'
  },
  {
    names: {en: 'Electronics', tr: 'Elektronik '},
    parent: '/',
    category: '/electronics',
    iconLigature: 'devices_baseline',
    iconFontFamily: 'MaterialIcons',
    isTag: false,
    _class: 'category'
  },
  {
    names: {en: 'Video Cards', tr: 'Ekran Kartı'},
    parent: '/computers',
    category: '/computers/video-cards',
    iconLigature: 'memory_baseline',
    iconFontFamily: 'MaterialIcons',
    isTag: false,
    _class: 'category'
  },
])

db.stores.insertMany([
  {
    name: 'Amazon',
    logo: 'https://logoeps.com/wp-content/uploads/2011/05/amazon-logo-vector.png',
    _class: 'store'
  },
  {
    name: 'BestBuy',
    logo: 'https://upload.wikimedia.org/wikipedia/commons/thumb/f/f5/Best_Buy_Logo.svg/1280px-Best_Buy_Logo.svg.png',
    _class: 'store'
  },
  {
    name: 'Walmart',
    logo: 'https://upload.wikimedia.org/wikipedia/commons/thumb/c/ca/Walmart_logo.svg/800px-Walmart_logo.svg.png',
    _class: 'store'
  },
])

db.categories.insertMany([
  {
    names: {en: 'Sale', tr: 'İndirim'},
    parent: '/',
    category: '/sale',
    iconLigature: 'local_offer_baseline',
    iconFontFamily: 'MaterialIcons',
    isTag: true,
    _class: 'category'
  },
  {
    names: {en: 'Discount', tr: 'İndirim'},
    parent: '/',
    category: '/discount',
    iconLigature: 'money_off_baseline',
    iconFontFamily: 'MaterialIcons',
    isTag: true,
    _class: 'category'
  },
  {
    names: {en: 'Free Shipping', tr: 'Ücretsiz Kargo'},
    parent: '/',
    category: '/free-shipping',
    iconLigature: 'local_shipping_baseline',
    iconFontFamily: 'MaterialIcons',
    isTag: true,
    _class: 'category'
  },
  {
    names: {en: 'Limited Time', tr: 'Sınırlı Süre'},
    parent: '/',
    category: '/limited-time',
    iconLigature: 'schedule_baseline',
    iconFontFamily: 'MaterialIcons',
    isTag: true,
    _class: 'category'
  },
  {
    names: {en: 'Clearance', tr: 'Stok Temizleme'},
    parent: '/',
    category: '/clearance',
    iconLigature: 'delete_sweep_baseline',
    iconFontFamily: 'MaterialIcons',
    isTag: true,
    _class: 'category'
  }
])


db.users.insertOne({
  "uid": "b91zCBlN7yNDph7mT9XRiPAaWGd2",  // The UID from your Firebase token
  "email": "pumasemj@hotmail.com",
  "nickname": "MrWaylon677",
  "avatar": "https://www.gravatar.com/avatar",
  "createdAt": new Date(),
  "_class": "user"  // This might be important for Spring Data MongoDB
})


// Get references to inserted documents
var amazonStore = db.stores.findOne({name: 'Amazon'})
var bestBuyStore = db.stores.findOne({name: 'BestBuy'})
var walmartStore = db.stores.findOne({name: 'Walmart'})


var user = db.users.findOne()


db.deals.insertMany([
  {
    postedBy: user._id,
    store: amazonStore._id,
    dealScore: 15,
    upvoters: [],
    downvoters: [],
    category: '/computers/video-cards',
    title: 'NVIDIA GeForce RTX 3080 Graphics Card - Limited Stock Available',
    description: 'The GeForce RTX 3080 delivers the ultra performance that gamers crave, powered by Ampere—NVIDIA\'s 2nd gen RTX architecture. It\'s built with enhanced RT Cores and Tensor Cores, new streaming multiprocessors, and superfast G6X memory for an amazing gaming experience.',
    originalPrice: 699.99,
    price: 649.99,
    coverPhoto: 'https://m.media-amazon.com/images/I/81qV-i5gBwL._AC_SL1500_.jpg',
    dealUrl: 'https://www.amazon.com/NVIDIA-GeForce-RTX-3080-Graphics/dp/B08HR6ZBYJ',
    status: 'ACTIVE',
    photos: [
      'https://m.media-amazon.com/images/I/81qV-i5gBwL._AC_SL1500_.jpg',
      'https://m.media-amazon.com/images/I/81wkDEMzj+L._AC_SL1500_.jpg'
    ],
    views: 120,
    tags: ['/sale', '/limited-time'],
    createdAt: new Date(),
    updatedAt: new Date(),
    _class: 'deal'
  },
  {
    postedBy: user._id,
    store: bestBuyStore._id,
    dealScore: 8,
    upvoters: [],
    downvoters: [],
    category: '/electronics',
    title: 'Sony WH-1000XM4 Wireless Noise-Cancelling Headphones',
    description: 'Industry-leading noise cancellation technology means you hear every word, note, and tune with incredible clarity, no matter your environment. These headphones feature additional microphones that assist in isolating sound for a more precise listening experience.',
    originalPrice: 349.99,
    price: 278.00,
    coverPhoto: 'https://pisces.bbystatic.com/image2/BestBuy_US/images/products/6408/6408356_sd.jpg',
    dealUrl: 'https://www.bestbuy.com/site/sony-wh-1000xm4-wireless-noise-cancelling-over-the-ear-headphones-black/6408356.p',
    status: 'ACTIVE',
    photos: [
      'https://pisces.bbystatic.com/image2/BestBuy_US/images/products/6408/6408356_sd.jpg',
      'https://pisces.bbystatic.com/image2/BestBuy_US/images/products/6408/6408356cv11d.jpg'
    ],
    views: 85,
    tags: ['/discount', '/free-shipping'],
    createdAt: new Date(),
    updatedAt: new Date(),
    _class: 'deal'
  },
  {
    postedBy: user._id,
    store: walmartStore._id,
    dealScore: 23,
    upvoters: [],
    downvoters: [],
    category: '/computers',
    title: 'HP 24mh FHD Monitor with 23.8-Inch IPS Display (1080p)',
    description: 'OUTSTANDING VISUALS – This FHD display with IPS technology gives you brilliant visuals and unforgettable quality; with a maximum resolution of 1920 x 1080 at 75 Hz, you\'ll experience the image accuracy and wide-viewing spectrums of premium tablets and mobile devices.',
    originalPrice: 249.99,
    price: 226.99,
    coverPhoto: 'https://i5.walmartimages.com/asr/d2a52481-5f27-4d9f-9838-31f346c4e2af.4b5c2f091d5da2853e70a6c9facac8d3.jpeg',
    dealUrl: 'https://www.walmart.com/ip/HP-24mh-FHD-Monitor-with-23-8-Inch-IPS-Display-1080p/715520698',
    status: 'ACTIVE',
    photos: [
      'https://i5.walmartimages.com/asr/d2a52481-5f27-4d9f-9838-31f346c4e2af.4b5c2f091d5da2853e70a6c9facac8d3.jpeg',
      'https://i5.walmartimages.com/asr/bb164cdf-f281-4e52-a481-511de4a82ae0.b4b9fa8a32134f56a429231d426d7cbe.jpeg'
    ],
    views: 210,
    tags: ['/clearance', '/discount'],
    createdAt: new Date(),
    updatedAt: new Date(),
    _class: 'deal'
  }
])