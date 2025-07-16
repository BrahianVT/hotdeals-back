db.createCollection('categories', {capped: false})
db.createCollection('stores', {capped: false})
db.createCollection('users', {capped: false})
db.createCollection('deals', {capped: false})

db.categories.insertMany([
  {
    names: {en: 'Flores y Hortalizas', tr: 'Bilgisayar'},
    parent: '/',
    category: '/flores_Hortalizas',
    iconLigature: 'computer_baseline',
    iconFontFamily: 'MaterialIcons',
    isTag: false,
    _class: 'category'
  },
  {
    names: {en: 'Abarrotes y Víveres', tr: 'Elektronik '},
    parent: '/',
    category: '/abarrotes_Viveres',
    iconLigature: 'devices_baseline',
    iconFontFamily: 'MaterialIcons',
    isTag: false,
    _class: 'category'
  },
  {
    names: {en: 'Leches', tr: 'Ekran Kartı'},
    parent: '/abarrotes_Viveres',
    category: '/abarrotes_Viveres/leches',
    iconLigature: 'memory_baseline',
    iconFontFamily: 'MaterialIcons',
    isTag: false,
    _class: 'category'
  },
])

db.stores.insertMany([
  {
    name: 'CEDA',
    logo: 'https://logoeps.com/wp-content/uploads/2011/05/amazon-logo-vector.png',
    _class: 'store'
  },
])

db.categories.insertMany([
  {
    names: {en: 'Frutas y Legumbres', tr: 'İndirim'},
    parent: '/',
    category: '/frutas_Legumbres',
    iconLigature: 'local_offer_baseline',
    iconFontFamily: 'MaterialIcons',
    isTag: false,
    _class: 'category'
  },
  {
    names: {en: 'Pernocta', tr: 'İndirim'},
    parent: '/',
    category: '/pernocta',
    iconLigature: 'money_off_baseline',
    iconFontFamily: 'MaterialIcons',
    isTag: false,
    _class: 'category'
  },
  {
    names: {en: 'Subasta y Productores', tr: 'Ücretsiz Kargo'},
    parent: '/',
    category: '/subasta',
    iconLigature: 'local_shipping_baseline',
    iconFontFamily: 'MaterialIcons',
    isTag: false,
    _class: 'category'
  },
  {
    names: {en: 'Envases Vacios', tr: 'Sınırlı Süre'},
    parent: '/',
    category: '/envases_Vacios',
    iconLigature: 'schedule_baseline',
    iconFontFamily: 'MaterialIcons',
    isTag: false,
    _class: 'category'
  },
  {
    names: {en: 'Aves y Cárnicos', tr: 'Stok Temizleme'},
    parent: '/',
    category: '/carnicos',
    iconLigature: 'delete_sweep_baseline',
    iconFontFamily: 'MaterialIcons',
    isTag: false,
    _class: 'category'
  },
  {
      names: {en: 'Viga', tr: 'Stok Temizleme'},
      parent: '/',
      category: '/viga',
      iconLigature: 'delete_sweep_baseline',
      iconFontFamily: 'MaterialIcons',
      isTag: false,
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
var ceda = db.stores.findOne({name: 'CEDA'})


var user = db.users.findOne()


db.deals.insertMany([
  {
    postedBy: user._id,
    store: ceda._id,
    dealScore: 15,
    upvoters: [],
    downvoters: [],
    category: '/flores_Hortalizas',
    title: 'Flores rojas por kilo',
    description: 'Flores rojas por kilo, en ceda',
    originalPrice: 699.99,
    price: 649.99,
    coverPhoto: 'https://m.media-amazon.com/images/I/81qV-i5gBwL._AC_SL1500_.jpg',
    dealUrl: 'https://www.amazon.com/NVIDIA-GeForce-RTX-3080-Graphics/dp/B08HR6ZBYJ',
    status: 'ACTIVE',
    photos: [
      'https://m.media-amazon.com/images/I/81qV-i5gBwL._AC_SL1500_.jpg',
      'https://m.media-amazon.com/images/I/81wkDEMzj+L._AC_SL1500_.jpg'
    ],
    views: 10,
    tags: ['/flores'],
    createdAt: new Date(),
    _class: 'deal'
  },
  {
    postedBy: user._id,
    store: ceda._id,
    dealScore: 8,
    upvoters: [],
    downvoters: [],
    category: '/abarrotes_Viveres',
    title: 'Oferta en sector de abarrotes',
    description: 'Oferta en sector de abarrotes',
    originalPrice: 349.99,
    price: 278.00,
    coverPhoto: 'https://placehold.co/600x400',
    dealUrl: 'https://www.bestbuy.com/site/sony-wh-1000xm4-wireless-noise-cancelling-over-the-ear-headphones-black/6408356.p',
    status: 'ACTIVE',
    photos: [
      'https://placehold.co/600x400',
      'https://placehold.co/600x400'
    ],
    views: 85,
    createdAt: new Date(),
    _class: 'deal'
  },
  {
    postedBy: user._id,
    store: ceda._id,
    dealScore: 23,
    upvoters: [],
    downvoters: [],
    category: '/frutas_Legumbres',
    title: 'Manzana Verde',
    description: 'Manzanas verdes baratas',
    originalPrice: 54.99,
    price: 50,
    coverPhoto: '',
    dealUrl: 'https://www.walmart.com/ip/HP-24mh-FHD-Monitor-with-23-8-Inch-IPS-Display-1080p/715520698',
    status: 'ACTIVE',
    photos: [
      'https://placehold.co/400',
      'https://placehold.co/400'
    ],
    views: 210,
    tags: ['/manzana'],
    createdAt: new Date(),
    updatedAt: new Date(),
    _class: 'deal'
  },
  {
      postedBy: user._id,
      store: ceda._id,
      dealScore: 100,
      upvoters: [],
      downvoters: [],
      category: '/pernocta',
      title: 'pernocta oferta pro',
      description: 'Pernocta',
      originalPrice: 54.99,
      price: 50,
      coverPhoto: '',
      dealUrl: 'https://www.walmart.com/ip/HP-24mh-FHD-Monitor-with-23-8-Inch-IPS-Display-1080p/715520698',
      status: 'ACTIVE',
      photos: [
        'https://placehold.co/400',
        'https://placehold.co/400'
      ],
      views: 210,
      createdAt: new Date(),
      _class: 'deal'
    },
  {
          postedBy: user._id,
          store: ceda._id,
          dealScore: 100,
          upvoters: [],
          downvoters: [],
          category: '/subasta',
          title: 'subasta oferta pro',
          description: 'subasta',
          originalPrice: 24.99,
          price: 20,
          coverPhoto: 'https://placehold.co/400',
          dealUrl: 'https://www.walmart.com/ip/HP-24mh-FHD-Monitor-with-23-8-Inch-IPS-Display-1080p/715520698',
          status: 'ACTIVE',
          photos: [
            'https://placehold.co/400',
            'https://placehold.co/400'
          ],
          views: 0,
          createdAt: new Date(),
          _class: 'deal'
        },
  {
                  postedBy: user._id,
                  store: ceda._id,
                  dealScore: 100,
                  upvoters: [],
                  downvoters: [],
                  category: '/envases_Vacios',
                  title: 'envases vacios',
                  description: 'envases',
                  originalPrice: 24.99,
                  price: 20,
                  coverPhoto: 'https://placehold.co/400',
                  dealUrl: 'https://www.walmart.com/ip/HP-24mh-FHD-Monitor-with-23-8-Inch-IPS-Display-1080p/715520698',
                  status: 'ACTIVE',
                  photos: [
                    'https://placehold.co/400',
                    'https://placehold.co/400'
                  ],
                  views: 0,
                  createdAt: new Date(),
                  _class: 'deal'
                },
  {
                                  postedBy: user._id,
                                  store: ceda._id,
                                  dealScore: 100,
                                  upvoters: [],
                                  downvoters: [],
                                  category: '/carnicos',
                                  title: 'kilo de pechuga',
                                  description: 'pechuga',
                                  originalPrice: 124.99,
                                  price: 120,
                                  coverPhoto: 'https://placehold.co/400',
                                  dealUrl: 'https://www.walmart.com/ip/HP-24mh-FHD-Monitor-with-23-8-Inch-IPS-Display-1080p/715520698',
                                  status: 'ACTIVE',
                                  photos: [
                                    'https://placehold.co/400',
                                    'https://placehold.co/400'
                                  ],
                                  views: 0,
                                  createdAt: new Date(),
                                  _class: 'deal'
                                },
  {
                                                                  postedBy: user._id,
                                                                  store: ceda._id,
                                                                  dealScore: 100,
                                                                  upvoters: [],
                                                                  downvoters: [],
                                                                  category: ' /viga',
                                                                  title: 'pescado kilo',
                                                                  description: 'pescado kilo',
                                                                  originalPrice: 124.99,
                                                                  price: 120,
                                                                  coverPhoto: 'https://placehold.co/400',
                                                                  dealUrl: 'https://www.walmart.com/ip/HP-24mh-FHD-Monitor-with-23-8-Inch-IPS-Display-1080p/715520698',
                                                                  status: 'ACTIVE',
                                                                  photos: [
                                                                    'https://placehold.co/400',
                                                                    'https://placehold.co/400'
                                                                  ],
                                                                  views: 0,
                                                                  createdAt: new Date(),
                                                                  _class: 'deal'
                                                                }
])