String[] masters = [
  'alpha',
  'bravo',
  'charlie',
  'delta',
  'echo'
]

masters.each{ master ->
  String uuid = UUID.randomUUID().toString();
  println("Encrypted UUID for ${master}: " + new hudson.util.Secret(uuid).getEncryptedValue());
}