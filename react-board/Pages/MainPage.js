import {tip} from '../data.json';
import CardView from '../Components/CardView';
import { StyleSheet, Text, View, Image, ScrollView, Dimensions, TouchableOpacity, SafeAreaView} from 'react-native';
import { useEffect, useState } from 'react';
import LoadingPage from './LoadingPage';

const mainImg = 'https://storage.googleapis.com/sparta-image.appspot.com/lecture/main.png';
const category = ['생활', '반려견', '재테크', '꿀팁 찜'];

export default function MainPage() {
    const [dataSet, isDataSet] = useState(false)
    const [data, setData] = useState([])
    const [cate, setCate] = useState('전체')

    useEffect(()=>{
      setTimeout(() => {

      })

      setData(tip)
      isDataSet(true)        

    },[cate])

    return(
      isDataSet?
      <SafeAreaView style={styles.safeAreaContainer}>
        <ScrollView style={styles.scrollViewContainer}>
            <View style={styles.containerOne}>
              <Text style={styles.category}>나만의 꿀팁</Text>
              <Image source={{uri:mainImg}}
                    style={{width: Dimensions.get('screen').width-10, height:200, borderRadius:20}}></Image>
            </View>
      
            <View style={styles.containerTwo}>
              <ScrollView horizontal={true}>
                {
                  category.map((value, i) => {
                    return(
                      <TouchableOpacity style={styles.scrollBtn} key={i}>
                        <Text style={styles.scrollBtnText} onPress={()=> (setCate(value))}>{value}</Text>
                      </TouchableOpacity>
                    )
                  })
                }
              </ScrollView>       
      
              <View style={styles.postBoard}>
                  {data.filter((content => content.category == cate )).map((value, i)=> {
                    return(
                      <CardView value={value} key={i}/>
                    )
                  })}
              </View>
            </View>
          </ScrollView>
        </SafeAreaView>
    : <LoadingPage/>
    )

}


const styles = StyleSheet.create({
    safeAreaContainer: {
      flex: 1,
    },
    scrollViewContainer: {
      backgroundColor: '#fff'
    },
    category: {
      fontWeight: 500,
      paddingVertical: 5,
      fontSize: 20,
    },
    containerOne: {
      flex: 1,
      alignItems: 'flex-start',
      paddingLeft: 5,
      paddingRight: 5
    },
    containerTwo: {
      flex: 4,
      flexDirection: 'column',
      paddingTop: 8,
      paddingHorizontal: 3
    }, 
    horizonalScroll: {
      flex: 1,
      flexDirection: 'row'
    },
    scrollBtn: {
      flex:1,
      flexDirection: 'column',
      justifyContent: 'center',
      width: 100,
      height: 50,
      borderRadius: 12,
      backgroundColor:'#d4531e',
      margin: 2
    },
    scrollBtnText: {
      color: '#fff',
      textAlign: 'center',
    },
    postBoard: {
      flex: 5,
      flexDirection: 'column',
      paddingHorizontal: 2,
      paddingTop: 5
    },
  });