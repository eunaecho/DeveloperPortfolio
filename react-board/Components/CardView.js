import React from 'react';
import { View, Text, StyleSheet, Image, TouchableOpacity, Dimensions } from 'react-native'
import { useNavigation } from '@react-navigation/native';

export default function CardView({value}) {
  const navigation = useNavigation();

  const screenWidth = Dimensions.get('screen').width;
  const imageWidth = 100; // 이미지 너비


  return (
    <TouchableOpacity style={cardStyle.container} 
      onPress={()=> { navigation.navigate('DetailPage', { detailTip: value }); }}>
      <Image source={{uri:value.image}} 
              style={{width: 100, height: 80, margin: 2, borderRadius: 10}}></Image>
      <View style={cardStyle.textPost}>
      <Text style={[cardStyle.textTitle, { width: screenWidth - imageWidth - 10 }]}>{value.title}</Text>
      <Text style={[cardStyle.textDesc, { width: screenWidth - imageWidth - 10 }]} numberOfLines={3}>{value.desc}</Text>
      <Text style={cardStyle.textDate}>{value.date}</Text>
      </View>
    </TouchableOpacity>
  )
}

const cardStyle = StyleSheet.create({
      container: {
        flexDirection: 'row',
        // width: Dimensions.get('screen').width,
        marginBottom: 2
      },
      textPost: {
        flexDirection: 'column',
        paddingHorizontal: 3
      },
      textTitle: {
        // width: Dimensions.get('screen').width -110 ,
        fontSize: 17,
        fontWeight: 700
      },
      textDesc: {
        // maxWidth: Dimensions.get('screen').width - 120,
        fontSize: 13,
        paddingRight: 3,
        color: '#090909'
      },
      textDate: {
        fontSize: 12,
        color: '#d3d3d3'
      }
})